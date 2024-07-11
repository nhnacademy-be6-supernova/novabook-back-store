package store.novabook.store.cart.service.impl;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import store.novabook.store.cart.repository.RedisCartRepository;
import store.novabook.store.orders.dto.OrderSagaMessage;
import store.novabook.store.orders.dto.request.OrderTemporaryNonMemberForm;
import store.novabook.store.orders.repository.RedisOrderNonMemberRepository;

@RequiredArgsConstructor
@Service
public class CartBookRabbitServiceImpl {

	private final RedisOrderNonMemberRepository redisOrderNonMemberRepository;
	private final RedisCartRepository redisCartRepository;

	// @RabbitListener(queues = "nova.cart.delete.queue")
	public void deleteCart(@Payload OrderSagaMessage orderSagaMessage) {

		// 비회원이면
		if (orderSagaMessage.getPaymentRequest().memberId() == null) {
			OrderTemporaryNonMemberForm orderForm = redisOrderNonMemberRepository.findById(
				orderSagaMessage.getPaymentRequest().orderId()).get();
			redisCartRepository.deleteById(orderForm.cartUUID());
		} else {
			redisCartRepository.deleteById(orderSagaMessage.getPaymentRequest().memberId());
		}
	}

}
