package store.novabook.store.point.service.impl;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.common.exception.NotFoundException;
import store.novabook.store.member.entity.Member;
import store.novabook.store.member.entity.MemberGradeHistory;
import store.novabook.store.member.repository.MemberGradeHistoryRepository;
import store.novabook.store.member.repository.MemberGradePolicyRepository;
import store.novabook.store.member.repository.MemberRepository;
import store.novabook.store.orders.dto.OrderSagaMessage;
import store.novabook.store.orders.dto.request.OrderTemporaryForm;
import store.novabook.store.orders.repository.RedisOrderRepository;
import store.novabook.store.point.entity.PointHistory;
import store.novabook.store.point.entity.PointPolicy;
import store.novabook.store.point.repository.PointHistoryRepository;
import store.novabook.store.point.repository.PointPolicyRepository;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PointHistoryRabbitServiceImpl {

	private final MemberRepository memberRepository;
	private final PointHistoryRepository pointHistoryRepository;
	private final PointPolicyRepository pointPolicyRepository;
	private final RedisOrderRepository redisOrderRepository;
	private final RabbitTemplate rabbitTemplate;
	private final MemberGradeHistoryRepository memberGradeHistoryRepository;


	/**
	 * 적립 포인트를 저장하는 메서드
	 */
	@RabbitListener(queues = "nova.point.earn.queue", containerFactory = "rabbitListenerContainerFactory")
	public void earnPoint(@Payload OrderSagaMessage orderSagaMessage) {
		Long memberId = orderSagaMessage.getPaymentRequest().memberId();

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + memberId));

		MemberGradeHistory memberGradeHistory = memberGradeHistoryRepository.findFirstByMemberIdOrderByCreatedAtDesc(memberId)
			.orElseThrow(() -> new IllegalArgumentException("MemberGradeHistory not found for member id: " + memberId));

		PointPolicy pointPolicy = pointPolicyRepository.findTopByOrderByCreatedAtDesc()
			.orElseThrow(() -> new IllegalArgumentException("PointPolicy not found"));

		long pointPercent = pointPolicy.getBasicPoint() + memberGradeHistory.getMemberGradePolicy().getDiscountRate();

		if(pointPercent >= 100) {
			throw new IllegalArgumentException("포인트 적립률이 100%가 넘습니다");
		}

		long earnPointAmount = orderSagaMessage.getCalculateTotalAmount() * pointPercent;

		pointHistoryRepository.save(PointHistory.of(pointPolicy, member, "주문으로 인한 포인트 적립", earnPointAmount));
	}



	@RabbitListener(queues = "nova.point.decrement.queue")
	public void decrementPoint(@Payload OrderSagaMessage orderSagaMessage) {
		try {
			Long memberId = orderSagaMessage.getPaymentRequest().memberId();

			// 주문 정보 조회
			OrderTemporaryForm orderTemporaryForm = redisOrderRepository.findById(memberId)
				.orElseThrow(() -> new IllegalArgumentException("주문 정보가 없습니다."));

			Long availablePoint = pointHistoryRepository.findTotalPointAmountByMemberId(memberId);

			if(availablePoint < orderTemporaryForm.usePointAmount()) {
				throw new IllegalArgumentException("포인트 잔액이 부족합니다");
			}

			// 회원 정보 조회
			Member member = memberRepository.findById(memberId)
				.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

			// 최근 포인트 정책 조회
			PointPolicy pointPolicy = pointPolicyRepository.findTopByOrderByCreatedAtDesc()
				.orElseThrow(() -> new IllegalArgumentException("포인트 정책을 조회할 수 없습니다"));

			// 포인트 기록 생성 및 저장
			PointHistory pointHistory = PointHistory.of(pointPolicy, member, "주문 포인트 사용", -1 * orderTemporaryForm.usePointAmount());
			pointHistoryRepository.save(pointHistory);

			// 포인트 할인 값 기록
			long pointDiscount = orderSagaMessage.getCalculateTotalAmount() - orderTemporaryForm.usePointAmount();
			orderSagaMessage.setCalculateTotalAmount(pointDiscount);

			// 성공 메시지 전송
			orderSagaMessage.setStatus("SUCCESS_POINT_DECREMENT");
			rabbitTemplate.convertAndSend("nova.orders.saga.exchange", "nova.api3-producer-routing-key", orderSagaMessage);
		} catch (Exception e) {
			handleFailure(orderSagaMessage);
			throw e;
		}
	}

	private void handleFailure(OrderSagaMessage orderSagaMessage) {
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		orderSagaMessage.setStatus("FAIL_POINT_DECREMENT");
		rabbitTemplate.convertAndSend("nova.orders.saga.exchange", "nova.api2-producer-routing-key", orderSagaMessage);
	}


	@RabbitListener(queues = "nova.point.compensate.decrement.queue")
	public void compensateDecrementPoint(@Payload OrderSagaMessage orderSagaMessage) {
		try {
			Long memberId = orderSagaMessage.getPaymentRequest().memberId();

			// 주문 정보 조회
			OrderTemporaryForm orderTemporaryForm = redisOrderRepository.findById(memberId)
				.orElseThrow(() -> new IllegalArgumentException("주문 정보가 없습니다."));

			// 회원 정보 조회
			Member member = memberRepository.findById(memberId)
				.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

			// 최근 포인트 정책 조회
			PointPolicy pointPolicy = pointPolicyRepository.findTopByOrderByCreatedAtDesc()
				.orElseThrow(() -> new IllegalArgumentException("포인트 정책을 조회할 수 없습니다"));

			// 포인트 기록 생성 및 저장 (포인트 복구)
			PointHistory pointHistory = PointHistory.of(pointPolicy, member, "결제 취소로 인한 주문 포인트 환불", orderTemporaryForm.usePointAmount());
			pointHistoryRepository.save(pointHistory);

			// 성공 메시지 전송
			orderSagaMessage.setStatus("SUCCESS_DECREMENT_POINT_COMPENSATE");
			log.info("SUCCESS_DECREMENT_POINT_COMPENSATE");
		} catch (Exception e) {
			handleFailureCompensate(orderSagaMessage);
			throw e;
		}
	}

	private void handleFailureCompensate(OrderSagaMessage orderSagaMessage) {
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		orderSagaMessage.setStatus("FAIL_DECREMENT_POINT_COMPENSATE");
		rabbitTemplate.convertAndSend("nova.orders.saga.exchange", "nova.orders.saga.dead.routing.key", orderSagaMessage);
	}
}
