package store.novabook.store.orders.service.impl;

import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import store.novabook.store.orders.dto.OrderSagaMessage;
import store.novabook.store.orders.dto.PaymentType;
import store.novabook.store.orders.dto.SagaMessage;
import store.novabook.store.orders.dto.request.PaymentRequest;

@RequiredArgsConstructor
@Slf4j
@Service
public class OrdersSagaManagerImpl {

	public static final String NOVA_ORDERS_SAGA_EXCHANGE = "nova.orders.saga.exchange";
	private final RabbitTemplate rabbitTemplate;


	/**
	 * 주문 로직 (트랜잭션) -> 비회원도 고려해야함
	 * 0. 주문서 검증 (총 결제 금액)
	 * 1. 재고 감소 시작
	 * 2. 포인트 감소
	 * 3. 쿠폰 사용 상태 변경
	 * 3.a -> 총 결제 가격 검증
	 * 4. 결제 승인
	 * <비동기 처리>
	 * 적립포인트 충전 -> 등급 별로 다르게 적용 되어야함
	 * 장바구니 제거
	 * 가주문 제거
	 * 메모: 마지막 로직에 토탈 금액 비교하는 로직 필요
	 */

	// 첫번째 로직 (가주문 검증, 비동기처리 전송)
	public void orderInvoke(PaymentRequest paymentRequest) {
		// 주문 트랜잭션 시작 (가주문 검증)
		rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "orders.form.verify.routing.key",
			OrderSagaMessage.builder().status("PROCEED_CONFIRM_ORDER_FORM").paymentRequest(paymentRequest).build());

		// 장바구니 제거
		rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "cart.delete.routing.key",
			OrderSagaMessage.builder().status("PROCEED_DELELTE_CART").paymentRequest(paymentRequest).build());

	}

	// 두번째 로직 (쿠폰 적용)
	@RabbitListener(queues = "nova.api1-producer-queue")
	public void handleApiResponse(@Payload OrderSagaMessage orderSagaMessage) {
		log.info("트랜잭션 진행 상태: {} ", orderSagaMessage.getStatus());

		//주문폼 검증 완료된 상태
		if (orderSagaMessage.getStatus().equals("SUCCESS_CONFIRM_ORDER_FORM")) {

			// 쿠폰을 사용하지 않을 경우 -> 바로 포인트 로직
			if (!orderSagaMessage.isNoUsePoint()) {
				orderSagaMessage.setStatus("PROCEED_POINT_DECREMENT");
				rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "point.decrement.routing.key",
					orderSagaMessage);
			} else if(!orderSagaMessage.isNoUseCoupon()) {
				orderSagaMessage.setStatus("PROCEED_APPLY_COUPON");
				rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "coupon.apply.routing.key", orderSagaMessage);
			} else {
				orderSagaMessage.setStatus("PROCEED_APPROVE_PAYMENT");
				rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "orders.approve.payment.routing.key",
					orderSagaMessage);
			}
		} else if (orderSagaMessage.getStatus().equals("FAIL_CONFIRM_ORDER_FORM")) {
			rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "nova.orders.saga.dead.routing.key", orderSagaMessage);
			log.error("주문서 검증 실패");
		}
	}

	// 세번째 로직 (포인트 감소 진행)
	@RabbitListener(queues = "nova.api2-producer-queue")
	public void handleApi2Response(@Payload OrderSagaMessage orderSagaMessage) {
		log.info("트랜잭션 진행 상태: {} ", orderSagaMessage.getStatus());

		if (orderSagaMessage.getStatus().equals("SUCCESS_APPLY_COUPON")) {

			// 포인트 적용을 하지 않을 경우 -> 바로 결제 진행
			if (orderSagaMessage.isNoUsePoint()) {
				orderSagaMessage.setStatus("PROCEED_APPROVE_PAYMENT");
				rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "orders.approve.payment.routing.key",
					orderSagaMessage);
			} else {
				orderSagaMessage.setStatus("PROCEED_POINT_DECREMENT");
				rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "point.decrement.routing.key",
					orderSagaMessage);
			}
		} else if (orderSagaMessage.getStatus().equals("FAIL_APPLY_COUPON")) {
			rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "nova.orders.saga.dead.routing.key", orderSagaMessage);
			log.error("[주문:쿠폰 적용 실패] 보상 트랜잭션을 시작합니다.");

			rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "compensate.orders.form.confirm.routing.key", orderSagaMessage);
		}
	}

	// 네번째 로직 (포인트 적용)
	@RabbitListener(queues = "nova.api3-producer-queue")
	public void handleApi3Response(@Payload OrderSagaMessage orderSagaMessage) {
		log.info("트랜잭션 진행 상태: {} ", orderSagaMessage.getStatus());

		if (orderSagaMessage.getStatus().equals("SUCCESS_POINT_DECREMENT")) {
			orderSagaMessage.setStatus("PROCEED_APPROVE_PAYMENT");
			rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "orders.approve.payment.routing.key",
				orderSagaMessage);

		} else if(orderSagaMessage.getStatus().equals("FAIL_POINT_DECREMENT")) {
			rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "nova.orders.saga.dead.routing.key", orderSagaMessage);
			log.error("[주문:포인트 감소 실패] 보상 트랜잭션을 시작합니다.");

			if(!orderSagaMessage.isNoUseCoupon())
				rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "compensate.coupon.apply.routing.key", orderSagaMessage);
			rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "compensate.orders.form.confirm.routing.key", orderSagaMessage);
		}
	}

	// 네번째 로직 (결제 승인)
	@RabbitListener(queues = "nova.api4-producer-queue")
	public void handleApi4Response(@Payload OrderSagaMessage orderSagaMessage) {
		log.info("트랜잭션 진행 상태: {} ", orderSagaMessage.getStatus());

		if (orderSagaMessage.getStatus().equals("SUCCESS_APPROVE_PAYMENT")) {
			log.info("성공적으로 결제가 완료되었습니다");
			orderSagaMessage.setStatus("PROCEES_SAVE_ORDERS_DATABASE");
			rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "orders.save.database.routing.key", orderSagaMessage);
		} else if (orderSagaMessage.getStatus().equals("FAIL_APPROVE_PAYMENT")) {
			rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "nova.orders.saga.dead.routing.key", orderSagaMessage);
			log.error("[주문:결제 승인 실패] 보상 트랜잭션을 시작합니다.");

			if(!orderSagaMessage.isNoUseCoupon())
				rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "compensate.coupon.apply.routing.key", orderSagaMessage);
			if(!orderSagaMessage.isNoUsePoint())
				rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "compensate.point.decrement.routing.key", orderSagaMessage);
			rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "compensate.orders.form.confirm.routing.key", orderSagaMessage);
		}
	}

	// 다섯번째 로직 (성공 & DB 저장)
	@RabbitListener(queues = "nova.api5-producer-queue")
	public void handleApi5Response(@Payload OrderSagaMessage orderSagaMessage) {
		log.info("트랜잭션 진행 상태: {} ", orderSagaMessage.getStatus());

		if (!orderSagaMessage.isNoEarnPoint() && orderSagaMessage.getStatus().equals("SUCCESS_SAVE_ORDERS_DATABASE")) {
			orderSagaMessage.setStatus("PROCEED_EARN_POINT");
			rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "point.earn.routing.key", orderSagaMessage);
		} else if (orderSagaMessage.getStatus().equals("FAIL_SAVE_ORDERS_DATABASE")) {
			rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "nova.orders.saga.dead.routing.key", orderSagaMessage);
			log.error("[주문:DB 저장 실패] 보상 트랜잭션을 시작합니다.");

			if(!orderSagaMessage.isNoUseCoupon())
				rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "compensate.coupon.apply.routing.key", orderSagaMessage);
			if(!orderSagaMessage.isNoUsePoint())
				rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "compensate.point.decrement.routing.key", orderSagaMessage);

			rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "compensate.approve.payment.routing.key", orderSagaMessage);
			rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "compensate.orders.form.confirm.routing.key", orderSagaMessage);
		}
	}

	// 포인트 적립 시작
	@RabbitListener(queues = "nova.api6-producer-queue")
	public void handleApi6Response(@Payload OrderSagaMessage orderSagaMessage) {
		log.info("트랜잭션 진행 상태: {} ", orderSagaMessage.getStatus());

		if (orderSagaMessage.getStatus().equals("SUCCESS_EARN_POINT")) {
			orderSagaMessage.setStatus("SUCCESS_ALL_ORDER_SAGA");
			log.info("성공적으로 모든 주문 트랜잭션이 완료되었습니다");
		} else if (orderSagaMessage.getStatus().equals("FAIL_EARN_POINT")) {
			rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "nova.orders.saga.dead.routing.key", orderSagaMessage);
			log.error("[주문:DB 포인트 저장 실패]");
		}
	}

	/**
	 * 결제 취소 시
	 * 1.주문 테이블에 총 적립 포인트량 -> DDL 에 넣어야함
	 * 2.포인트는 사용량만큼 감소
	 * 3.쿠폰은 비사용 처리
	 * 4.
	 *
	 */
	public void orderCancel(UUID uuid) {
		// DB 상태 업데이트 ->
		// OrderSagaMessage.builder()
		// 이어서 구현하겠습니다.

		PaymentRequest.builder()
			.type(PaymentType.TOSS)
			.orderId(uuid)
			.memberId(1L)
			.build();











		rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "?");
	}



}
