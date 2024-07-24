package store.novabook.store.payment.service.impl;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Payload;

import store.novabook.store.common.util.PaymentFactory;
import store.novabook.store.orders.dto.OrderSagaMessage;
import store.novabook.store.orders.dto.RequestPayCancelMessage;
import store.novabook.store.payment.Payment;
import store.novabook.store.payment.service.PaymentService;

public class PaymentServiceImpl implements PaymentService {
	private final RabbitTemplate rabbitTemplate;
	private final PaymentFactory paymentFactory;

	public PaymentServiceImpl(RabbitTemplate rabbitTemplate, PaymentFactory paymentFactory) {
		this.rabbitTemplate = rabbitTemplate;
		this.paymentFactory = paymentFactory;
	}

	@Override
	@RabbitListener(queues = "nova.orders.approve.payment.queue")
	public void createOrder(@Payload OrderSagaMessage orderSagaMessage) {
		Payment payment = paymentFactory.getPaymentStrategy(orderSagaMessage.getPaymentRequest().type());
		payment.createOrder(orderSagaMessage, rabbitTemplate);
	}

	@Override
	@RabbitListener(queues = "nova.orders.compensate.approve.payment.queue")
	public void compensateCancelOrder(@Payload OrderSagaMessage orderSagaMessage) {
		Payment payment = paymentFactory.getPaymentStrategy(orderSagaMessage.getPaymentRequest().type());
		payment.compensateCancelOrder(orderSagaMessage, rabbitTemplate);
	}


	@Override
	@RabbitListener(queues = "nova.payment.request.pay.cancel.queue")
	public void cancelOrder(@Payload RequestPayCancelMessage message, RabbitTemplate rabbitTemplate) {
		Payment payment = paymentFactory.getPaymentStrategy(message.getPaymentType());
		payment.cancelOrder(message, rabbitTemplate);
	}
}
