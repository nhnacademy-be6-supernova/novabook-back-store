package store.novabook.store.payment;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.transaction.annotation.Transactional;

import store.novabook.store.orders.dto.OrderSagaMessage;
import store.novabook.store.orders.dto.RequestPayCancelMessage;

public interface Payment {
	@Transactional
	void createOrder(@Payload OrderSagaMessage orderSagaMessage, RabbitTemplate rabbitTemplate);

	@Transactional
	void compensateCancelOrder(@Payload OrderSagaMessage orderSagaMessage, RabbitTemplate rabbitTemplate);

	void cancelOrder(@Payload RequestPayCancelMessage message, RabbitTemplate rabbitTemplate);
}
