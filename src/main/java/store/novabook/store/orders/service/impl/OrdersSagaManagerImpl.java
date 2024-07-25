package store.novabook.store.orders.service.impl;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import store.novabook.store.orders.dto.OrderSagaMessage;
import store.novabook.store.orders.dto.RequestPayCancelMessage;
import store.novabook.store.orders.dto.request.PaymentRequest;

/**
 * 주문 사가 매니저 구현 클래스
 * 이 클래스는 주문 사가 패턴을 사용하여 비동기적으로 주문 트랜잭션을 관리합니다.
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class OrdersSagaManagerImpl {

	public static final String NOVA_ORDERS_SAGA_EXCHANGE = "nova.orders.saga.exchange";
	public static final String COMPENSATE_ORDERS_FORM_CONFIRM_ROUTING_KEY = "compensate.orders.form.confirm.routing.key";
	public static final String NOVA_ORDERS_SAGA_DEAD_ROUTING_KEY = "nova.orders.saga.dead.routing.key";
	public static final String STATUS_LOG_MESSAGE = "트랜잭션 진행 상태: {} ";
	public static final String PROCEED_APPROVE_PAYMENT = "PROCEED_APPROVE_PAYMENT";
	public static final String ORDERS_APPROVE_PAYMENT_ROUTING_KEY = "orders.approve.payment.routing.key";
	public static final String COMPENSATE_COUPON_APPLY_ROUTING_KEY = "compensate.coupon.apply.routing.key";
	public static final String PROCEED_SAVE_ORDERS_DATABASE = "PROCEED_SAVE_ORDERS_DATABASE";
	public static final String ORDERS_SAVE_DATABASE_ROUTING_KEY = "orders.save.database.routing.key";
	private final RabbitTemplate rabbitTemplate;
	private final MeterRegistry meterRegistry;

	private Counter orderCounter;
	private Counter orderFailureCounter;
	private Timer orderProcessingTimer;

	@PostConstruct
	public void initMetrics() {
		this.orderCounter = meterRegistry.counter("orders_total_count");
		this.orderFailureCounter = meterRegistry.counter("orders_failure_count");
		this.orderProcessingTimer = meterRegistry.timer("order_processing_time");
	}

	/**
	 * 첫번째 로직 (가주문 검증, 비동기처리 전송)
	 *
	 * @param paymentRequest 결제 요청 정보
	 */
	public void orderInvoke(PaymentRequest paymentRequest) {
		orderCounter.increment();
		Timer.Sample sample = Timer.start(meterRegistry);

		try {
			// 주문 트랜잭션 시작 (가주문 검증)
			rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "orders.form.verify.routing.key",
				OrderSagaMessage.builder().status("PROCEED_CONFIRM_ORDER_FORM").paymentRequest(paymentRequest).build());

			// 장바구니 제거
			rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "cart.delete.routing.key",
				OrderSagaMessage.builder().status("PROCEED_DELETE_CART").paymentRequest(paymentRequest).build());
		} catch (Exception e) {
			// 주문 실패 카운트
			orderFailureCounter.increment();
			throw e;
		} finally {
			sample.stop(orderProcessingTimer);
		}
	}

	/**
	 * 두번째 로직 (쿠폰 적용)
	 *
	 * @param orderSagaMessage 주문 사가 메시지
	 */
	@RabbitListener(queues = "nova.api1-producer-queue")
	public void handleApiResponse(@Payload OrderSagaMessage orderSagaMessage) {
		log.info(STATUS_LOG_MESSAGE, orderSagaMessage.getStatus());

		try {
			if ("SUCCESS_CONFIRM_ORDER_FORM".equals(orderSagaMessage.getStatus())) {
				boolean isNoUsePoint = orderSagaMessage.isNoUsePoint();
				boolean isNoUseCoupon = orderSagaMessage.isNoUseCoupon();

				if (isNoUsePoint && isNoUseCoupon) {
					// 결제금액이 없을 경우
					if (orderSagaMessage.getCalculateTotalAmount() == 0) {
						orderSagaMessage.setStatus(PROCEED_SAVE_ORDERS_DATABASE);
						rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, ORDERS_SAVE_DATABASE_ROUTING_KEY,
							orderSagaMessage);
					} else {
						orderSagaMessage.setStatus(PROCEED_APPROVE_PAYMENT);
						rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, ORDERS_APPROVE_PAYMENT_ROUTING_KEY,
							orderSagaMessage);
					}
				} else if (isNoUseCoupon) {
					orderSagaMessage.setStatus("PROCEED_POINT_DECREMENT");
					rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "point.decrement.routing.key",
						orderSagaMessage);
				} else {
					orderSagaMessage.setStatus("PROCEED_APPLY_COUPON");
					rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "coupon.apply.routing.key",
						orderSagaMessage);
				}
			} else if ("FAIL_CONFIRM_ORDER_FORM".equals(orderSagaMessage.getStatus())) {
				rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, NOVA_ORDERS_SAGA_DEAD_ROUTING_KEY,
					orderSagaMessage);
				log.error("주문서 검증 실패");
			}
		} catch (Exception e) {
			orderFailureCounter.increment();
			throw e;
		}
	}

	/**
	 * 세번째 로직 (포인트 감소 진행)
	 *
	 * @param orderSagaMessage 주문 사가 메시지
	 */
	@RabbitListener(queues = "nova.api2-producer-queue")
	public void handleApi2Response(@Payload OrderSagaMessage orderSagaMessage) {
		log.info(STATUS_LOG_MESSAGE, orderSagaMessage.getStatus());

		if (orderSagaMessage.getStatus().equals("SUCCESS_APPLY_COUPON")) {

			// 포인트 적용을 하지 않을 경우 -> 바로 결제 진행
			if (orderSagaMessage.isNoUsePoint()) {
				// 결제금액이 없을 경우
				if (orderSagaMessage.getCalculateTotalAmount() == 0) {
					orderSagaMessage.setStatus(PROCEED_SAVE_ORDERS_DATABASE);
					rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, ORDERS_SAVE_DATABASE_ROUTING_KEY,
						orderSagaMessage);
				} else {
					orderSagaMessage.setStatus(PROCEED_APPROVE_PAYMENT);
					rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, ORDERS_APPROVE_PAYMENT_ROUTING_KEY,
						orderSagaMessage);
				}
			} else {
				orderSagaMessage.setStatus("PROCEED_POINT_DECREMENT");
				rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "point.decrement.routing.key",
					orderSagaMessage);
			}
		} else if (orderSagaMessage.getStatus().equals("FAIL_APPLY_COUPON")) {
			orderFailureCounter.increment();
			rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, NOVA_ORDERS_SAGA_DEAD_ROUTING_KEY,
				orderSagaMessage);
			log.error("[주문:쿠폰 적용 실패] 보상 트랜잭션을 시작합니다.");

			rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, COMPENSATE_ORDERS_FORM_CONFIRM_ROUTING_KEY,
				orderSagaMessage);
		}
	}

	/**
	 * 네번째 로직 (포인트 적용)
	 *
	 * @param orderSagaMessage 주문 사가 메시지
	 */
	@RabbitListener(queues = "nova.api3-producer-queue")
	public void handleApi3Response(@Payload OrderSagaMessage orderSagaMessage) {
		log.info(STATUS_LOG_MESSAGE, orderSagaMessage.getStatus());

		if (orderSagaMessage.getStatus().equals("SUCCESS_POINT_DECREMENT")) {
			// 결제금액이 없을 경우
			if (orderSagaMessage.getCalculateTotalAmount() == 0) {
				orderSagaMessage.setStatus(PROCEED_SAVE_ORDERS_DATABASE);
				rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, ORDERS_SAVE_DATABASE_ROUTING_KEY,
					orderSagaMessage);
			} else {
				orderSagaMessage.setStatus(PROCEED_APPROVE_PAYMENT);
				rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, ORDERS_APPROVE_PAYMENT_ROUTING_KEY,
					orderSagaMessage);
			}
		} else if (orderSagaMessage.getStatus().equals("FAIL_POINT_DECREMENT")) {
			orderFailureCounter.increment();
			rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, NOVA_ORDERS_SAGA_DEAD_ROUTING_KEY,
				orderSagaMessage);
			log.error("[주문:포인트 감소 실패] 보상 트랜잭션을 시작합니다.");

			if (!orderSagaMessage.isNoUseCoupon())
				rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, COMPENSATE_COUPON_APPLY_ROUTING_KEY,
					orderSagaMessage);
			rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, COMPENSATE_ORDERS_FORM_CONFIRM_ROUTING_KEY,
				orderSagaMessage);
		}
	}

	/**
	 * 네번째 로직 (결제 승인)
	 *
	 * @param orderSagaMessage 주문 사가 메시지
	 */
	@RabbitListener(queues = "nova.api4-producer-queue")
	public void handleApi4Response(@Payload OrderSagaMessage orderSagaMessage) {
		log.info(STATUS_LOG_MESSAGE, orderSagaMessage.getStatus());

		if (orderSagaMessage.getStatus().equals("SUCCESS_APPROVE_PAYMENT")) {
			log.info("성공적으로 결제가 완료되었습니다");
			orderSagaMessage.setStatus(PROCEED_SAVE_ORDERS_DATABASE);
			rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, ORDERS_SAVE_DATABASE_ROUTING_KEY,
				orderSagaMessage);
		} else if (orderSagaMessage.getStatus().equals("FAIL_APPROVE_PAYMENT")) {
			orderFailureCounter.increment();
			rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, NOVA_ORDERS_SAGA_DEAD_ROUTING_KEY,
				orderSagaMessage);
			log.error("[주문:결제 승인 실패] 보상 트랜잭션을 시작합니다.");

			if (!orderSagaMessage.isNoUseCoupon())
				rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, COMPENSATE_COUPON_APPLY_ROUTING_KEY,
					orderSagaMessage);
			if (!orderSagaMessage.isNoUsePoint())
				rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "compensate.point.decrement.routing.key",
					orderSagaMessage);
			rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, COMPENSATE_ORDERS_FORM_CONFIRM_ROUTING_KEY,
				orderSagaMessage);
		}
	}

	/**
	 * 다섯번째 로직 (성공 & DB 저장)
	 *
	 * @param orderSagaMessage 주문 사가 메시지
	 */
	@RabbitListener(queues = "nova.api5-producer-queue")
	public void handleApi5Response(@Payload OrderSagaMessage orderSagaMessage) {
		log.info(STATUS_LOG_MESSAGE, orderSagaMessage.getStatus());

		if (!orderSagaMessage.isNoEarnPoint() && orderSagaMessage.getStatus().equals("SUCCESS_SAVE_ORDERS_DATABASE")) {
			orderSagaMessage.setStatus("PROCEED_EARN_POINT");
			rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "point.earn.routing.key", orderSagaMessage);
		} else if (orderSagaMessage.getStatus().equals("FAIL_SAVE_ORDERS_DATABASE")) {
			orderFailureCounter.increment();
			rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, NOVA_ORDERS_SAGA_DEAD_ROUTING_KEY,
				orderSagaMessage);
			log.error("[주문:DB 저장 실패] 보상 트랜잭션을 시작합니다.");

			if (!orderSagaMessage.isNoUseCoupon())
				rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, COMPENSATE_COUPON_APPLY_ROUTING_KEY,
					orderSagaMessage);
			if (!orderSagaMessage.isNoUsePoint())
				rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "compensate.point.decrement.routing.key",
					orderSagaMessage);
			if (orderSagaMessage.getCalculateTotalAmount() != 0)
				rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "compensate.approve.payment.routing.key",
					orderSagaMessage);
			rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, COMPENSATE_ORDERS_FORM_CONFIRM_ROUTING_KEY,
				orderSagaMessage);
		}
	}

	/**
	 * 포인트 적립 시작
	 *
	 * @param orderSagaMessage 주문 사가 메시지
	 */
	@RabbitListener(queues = "nova.api6-producer-queue")
	public void handleApi6Response(@Payload OrderSagaMessage orderSagaMessage) {
		log.info(STATUS_LOG_MESSAGE, orderSagaMessage.getStatus());

		if (orderSagaMessage.getStatus().equals("SUCCESS_EARN_POINT")) {
			orderSagaMessage.setStatus("SUCCESS_ALL_ORDER_SAGA");
			log.info("성공적으로 모든 주문 트랜잭션이 완료되었습니다");
		} else if (orderSagaMessage.getStatus().equals("FAIL_EARN_POINT")) {
			orderFailureCounter.increment();
			rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, NOVA_ORDERS_SAGA_DEAD_ROUTING_KEY,
				orderSagaMessage);
			log.error("[주문:DB 포인트 저장 실패]");
		}
	}

	/**
	 * 결제 취소 & 환불 비동기처리
	 *
	 * @param payCancelMessage 쿠폰, 포인트, 적립금 정보
	 */
	@RabbitListener(queues = "nova.request.pay.cancel.queue")
	public void requestPayCancel(@Payload RequestPayCancelMessage payCancelMessage) {
		if (payCancelMessage.getCouponId() != null)
			rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "coupon.request.pay.cancel.routing.key",
				payCancelMessage);
		if (payCancelMessage.getUsePointAmount() > 0)
			rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "point.request.pay.cancel.routing.key",
				payCancelMessage);
		if (payCancelMessage.getTotalAmount() != 0)
			rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "payment.pay.cancel.routing.key",
				payCancelMessage);

		rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "orders.request.pay.cancel.routing.key",
			payCancelMessage);
	}
}
