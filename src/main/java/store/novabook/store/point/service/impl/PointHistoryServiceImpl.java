package store.novabook.store.point.service.impl;

import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import lombok.RequiredArgsConstructor;
import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.common.exception.NotFoundException;
import store.novabook.store.member.entity.Member;
import store.novabook.store.member.repository.MemberRepository;
import store.novabook.store.orders.dto.OrderSagaMessage;
import store.novabook.store.orders.dto.request.OrderTemporaryForm;
import store.novabook.store.orders.repository.RedisOrderRepository;
import store.novabook.store.point.dto.request.CreatePointHistoryRequest;
import store.novabook.store.point.dto.request.GetPointHistoryRequest;
import store.novabook.store.point.dto.response.GetPointHistoryListResponse;
import store.novabook.store.point.dto.response.GetPointHistoryResponse;
import store.novabook.store.point.dto.response.GetPointResponse;
import store.novabook.store.point.entity.PointHistory;
import store.novabook.store.point.entity.PointPolicy;
import store.novabook.store.point.repository.PointHistoryRepository;
import store.novabook.store.point.repository.PointPolicyRepository;
import store.novabook.store.point.service.PointHistoryService;

@Service
@RequiredArgsConstructor
@Transactional
public class PointHistoryServiceImpl implements PointHistoryService {
	private final MemberRepository memberRepository;
	private final PointHistoryRepository pointHistoryRepository;
	private final PointPolicyRepository pointPolicyRepository;

	private final RabbitTemplate rabbitTemplate;
	private final RedisOrderRepository redisOrderRepository;

	@Override
	@Transactional(readOnly = true)
	public Page<GetPointHistoryResponse> getPointHistoryList(Pageable pageable) {
		Page<PointHistory> pointHistoryList = pointHistoryRepository.findAll(pageable);
		if (pointHistoryList.isEmpty()) {
			throw new NotFoundException(ErrorCode.POINT_HISTORY_NOT_FOUND);
		}
		return pointHistoryList.map(GetPointHistoryResponse::of);
	}

	@Override
	@Transactional(readOnly = true)
	public GetPointHistoryListResponse getPointHistory(GetPointHistoryRequest getPointHistoryRequest) {
		List<GetPointHistoryResponse> pointHistoryResponses = pointHistoryRepository.findByMemberId(
				getPointHistoryRequest.memberId())
			.stream()
			.map(pointHistory -> GetPointHistoryResponse.builder()
				.pointAmount(pointHistory.getPointAmount())
				.pointContent(pointHistory.getPointContent())
				.build())
			.toList();

		return GetPointHistoryListResponse.builder().pointHistoryResponseList(pointHistoryResponses).build();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<GetPointHistoryResponse> getPointHistoryByMemberIdPage(Long memberId, Pageable pageable) {
		Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
		pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		return pointHistoryRepository.findAllByMemberId(memberId, pageable);
	}

	@Override
	public void createPointHistory(CreatePointHistoryRequest createPointHistoryRequest) {
		Member member = memberRepository.findById(createPointHistoryRequest.memberId())
			.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

		PointPolicy pointPolicy = pointPolicyRepository.findById(createPointHistoryRequest.pointPolicyId())
			.orElseThrow(() -> new NotFoundException(ErrorCode.POINT_POLICY_NOT_FOUND));

		PointHistory pointHistory = PointHistory.of(pointPolicy, member,
			createPointHistoryRequest.pointContent(), createPointHistoryRequest.pointAmount());
		pointHistoryRepository.save(pointHistory);
	}

	@Override
	@Transactional(readOnly = true)
	public GetPointResponse getPointTotalByMemberId(Long memberId) {
		return GetPointResponse.builder()
			.pointAmount(pointHistoryRepository.findTotalPointAmountByMemberId(memberId))
			.build();
	}

	@RabbitListener(queues = "nova.point.decrement.queue")
	@Transactional
	public void decrementPoint(@Payload OrderSagaMessage orderSagaMessage) {
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

			// 포인트 기록 생성 및 저장
			PointHistory pointHistory = PointHistory.of(pointPolicy, member, "주문 포인트 사용", orderTemporaryForm.usePointAmount());
			pointHistoryRepository.save(pointHistory);

			// 성공 메시지 전송
			orderSagaMessage.setStatus("SUCCESS_POINT_DECREMENT");
			rabbitTemplate.convertAndSend("nova.orders.saga.exchange", "nova.api2-producer-routing-key", orderSagaMessage);
		} catch (Exception e) {
			handleFailure(orderSagaMessage);
			throw e;
		}
	}

	/**
	 * 트랜잭션 롤백 처리
	 * 실패 메시지 전송
	 * @param orderSagaMessage
	 */
	private void handleFailure(OrderSagaMessage orderSagaMessage) {
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		orderSagaMessage.setStatus("FAIL_POINT_DECREMENT");
		rabbitTemplate.convertAndSend("nova.orders.saga.exchange", "nova.api2-producer-routing-key", orderSagaMessage);
	}
}
