package store.novabook.store.payment.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Payload;

import store.novabook.store.orders.dto.OrderSagaMessage;
import store.novabook.store.orders.dto.RequestPayCancelMessage;

public interface PaymentService {
	@RabbitListener(queues = "nova.orders.approve.payment.queue")
	void createOrder(@Payload OrderSagaMessage orderSagaMessage);

	@RabbitListener(queues = "nova.orders.compensate.approve.payment.queue")
	void compensateCancelOrder(@Payload OrderSagaMessage orderSagaMessage);

	@RabbitListener(queues = "nova.payment.request.pay.cancel.queue")
	void cancelOrder(@Payload RequestPayCancelMessage message);
}
