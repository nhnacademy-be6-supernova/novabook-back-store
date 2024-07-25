package store.novabook.store.payment.service.impl;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import store.novabook.store.common.util.PaymentFactory;
import store.novabook.store.orders.dto.OrderSagaMessage;
import store.novabook.store.orders.dto.RequestPayCancelMessage;
import store.novabook.store.payment.Payment;
import store.novabook.store.payment.service.PaymentService;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

	public static final String NOVA_ORDERS_SAGA_DEAD_ROUTING_KEY = "nova.orders.saga.dead.routing.key";
	private final RabbitTemplate rabbitTemplate;
	private final PaymentFactory paymentFactory;
	public static final String NOVA_ORDERS_SAGA_EXCHANGE = "nova.orders.saga.exchange";

	@Override
	@Transactional
	@RabbitListener(queues = "nova.orders.approve.payment.queue")
	public void createOrder(@Payload OrderSagaMessage orderSagaMessage) {
		try {
			Payment payment = paymentFactory.getPaymentStrategy(orderSagaMessage.getPaymentRequest().type());
			payment.createOrder(orderSagaMessage);
			orderSagaMessage.setStatus("SUCCESS_APPROVE_PAYMENT");
		} catch (Exception e) {
			log.error("", e);
			orderSagaMessage.setStatus("FAIL_APPROVE_PAYMENT");
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, NOVA_ORDERS_SAGA_DEAD_ROUTING_KEY,
				orderSagaMessage);
		} finally {
			rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, "nova.api4-producer-routing-key",
				orderSagaMessage);
		}
	}

	@Override
	@Transactional
	@RabbitListener(queues = "nova.orders.compensate.approve.payment.queue")
	public void compensateCancelOrder(@Payload OrderSagaMessage orderSagaMessage) {
		try {
			Payment payment = paymentFactory.getPaymentStrategy(orderSagaMessage.getPaymentRequest().type());
			payment.compensateCancelOrder(orderSagaMessage);
		} catch (Exception e) {
			orderSagaMessage.setStatus("FAIL_REFUND_TOSS_PAYMENT");
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, NOVA_ORDERS_SAGA_DEAD_ROUTING_KEY,
				orderSagaMessage);
		}
	}

	@Override
	@RabbitListener(queues = "nova.payment.request.pay.cancel.queue")
	public void cancelOrder(@Payload RequestPayCancelMessage message) {
		try {
			Payment payment = paymentFactory.getPaymentStrategy(message.getPaymentType());
			payment.cancelOrder(message);
		} catch (Exception e) {
			log.error("",e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			message.setStatus("FAIL_REQUEST_CANCEL_TOSS_PAYMENT");
			rabbitTemplate.convertAndSend(NOVA_ORDERS_SAGA_EXCHANGE, NOVA_ORDERS_SAGA_DEAD_ROUTING_KEY, message);
		}
	}
}
