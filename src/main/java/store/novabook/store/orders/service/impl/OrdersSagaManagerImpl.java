package store.novabook.store.orders.service.impl;

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
	 */



	// 첫번째 로직 (가주문 검증)
	public void orderInvoke(PaymentRequest paymentRequest) {
		// 가주문 검증 진행
		rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "orders.form.confirm.routing.key",
			OrderSagaMessage.builder().status("PROCEED_CONFIRM_ORDER_FORM").paymentRequest(paymentRequest).build());
	}

	// 두번째 로직 (포인트 감소)
	@RabbitListener(queues = "nova.api1-producer-queue")
	public void handleApiResponse(@Payload OrderSagaMessage orderSagaMessage) {
		log.info("트랜잭션 진행 상태: {} ", orderSagaMessage.getStatus());

		//주문폼 검증 완료된 상태
		if (orderSagaMessage.getStatus().equals("SUCCESS_CONFIRM_ORDER_FORM")) {
			if (!orderSagaMessage.isNoUsePoint()) {
				orderSagaMessage.setStatus("PROCEED_POINT_DECREMENT");
				rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "point.decrement.routing.key", orderSagaMessage);
			} else {
				orderSagaMessage.setStatus("PROCEED_APPLY_COUPON");
				rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "coupon.apply.routing.key", orderSagaMessage);
			}
		} else if (orderSagaMessage.getStatus().equals("FAIL_CONFIRM_ORDER_FORM")) {
			log.error("주문서 검증 실패");
		}
	}



	// 세번째 로직 (쿠폰 적용)
	@RabbitListener(queues = "nova.api2-producer-queue")
	public void handleApi2Response(@Payload OrderSagaMessage orderSagaMessage) {
		log.info("트랜잭션 진행 상태: {} ", orderSagaMessage.getStatus());

		if(orderSagaMessage.getStatus().equals("SUCCESS_POINT_DECREMENT")) {
			if(!orderSagaMessage.isNoUseCoupon()) {
				orderSagaMessage.setStatus("PROCEED_APPLY_COUPON");
				rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "coupon.apply.routing.key", orderSagaMessage);
			} else {
				orderSagaMessage.setStatus("PROCEED_APPROVE_PAYMENT");
				rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "coupon.apply.routing.key", orderSagaMessage);
			}
		} else if (orderSagaMessage.getStatus().equals("FAIL_POINT_DECREMENT")) {
			log.error("포인트 감소 실패");
		}
	}


	// 세번째 로직 (결제 진행)
	@RabbitListener(queues = "nova.api3-producer-queue")
	public void handleApi3Response(@Payload OrderSagaMessage orderSagaMessage) {
		log.info("트랜잭션 진행 상태: {} ", orderSagaMessage.getStatus());

		if (!orderSagaMessage.isNoUsePoint() && orderSagaMessage.getStatus().equals("SUCCESS_APPLY_COUPON")) {
			orderSagaMessage.setStatus("PROCEED_APPROVE_PAYMENT");
			rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "coupon.apply.routing.key", orderSagaMessage);
		}  else if (orderSagaMessage.getStatus().equals("CONFIRM_FAIL")) {
			log.error("API1 execution failed");
		}
	}

	// 네번째 로직 (성공 & 마무리)
	// @RabbitListener(queues = "nova.api4-producer-queue")
	public void handleApi4Response(@Payload OrderSagaMessage orderSagaMessage) {
		log.info("트랜잭션 진행 상태: {} ", orderSagaMessage.getStatus());

		if (!orderSagaMessage.isNoUsePoint() && orderSagaMessage.getStatus().equals("SUCCESS_APPROVE_PAYMENT")) {
			orderSagaMessage.setStatus("SUCCESS_ALL_ORDER_SAGA");
			rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "완료시 가야할 큐", orderSagaMessage);
		}  else if (orderSagaMessage.getStatus().equals("FAIL_APPROVE_PAYMENT")) {
			log.error("API1 execution failed");
		}
	}

}
