package store.novabook.store.cart.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import store.novabook.store.cart.repository.RedisCartRepository;
import store.novabook.store.cart.service.CartBookService;
import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.common.exception.NotFoundException;
import store.novabook.store.orders.dto.OrderSagaMessage;
import store.novabook.store.orders.dto.request.BookIdAndQuantityDTO;
import store.novabook.store.orders.dto.request.OrderTemporaryForm;
import store.novabook.store.orders.dto.request.OrderTemporaryNonMemberForm;
import store.novabook.store.orders.repository.RedisOrderNonMemberRepository;
import store.novabook.store.orders.repository.RedisOrderRepository;

@RequiredArgsConstructor
@Service
public class CartBookRabbitServiceImpl {

	private final RedisOrderNonMemberRepository redisOrderNonMemberRepository;
	private final RedisOrderRepository redisOrderRepository;
	private final RedisCartRepository redisCartRepository;
	private final CartBookService cartBookService;

	@RabbitListener(queues = "nova.cart.delete.queue")
	public void deleteCart(@Payload OrderSagaMessage orderSagaMessage) {
		// 비회원이면
		if (orderSagaMessage.getPaymentRequest().memberId() == null) {
			Optional<OrderTemporaryNonMemberForm> optionalOrderForm = redisOrderNonMemberRepository.findById(
				orderSagaMessage.getPaymentRequest().orderCode());

			if(optionalOrderForm.isEmpty()) {
				throw new NotFoundException(ErrorCode.ORDERS_NOT_FOUND);
			}

			OrderTemporaryNonMemberForm orderForm = optionalOrderForm.get();
			redisCartRepository.deleteById(orderForm.cartUUID());
		} else {
			Long memberId = orderSagaMessage.getPaymentRequest().memberId();
			redisCartRepository.deleteById(memberId);
			Optional<OrderTemporaryForm> temporaryForm = redisOrderRepository.findById(
				memberId);

			if (temporaryForm.isEmpty()) {
				throw new NotFoundException(ErrorCode.CART_NOT_FOUND);
			}

			OrderTemporaryForm orderForm = temporaryForm.get();
			List<BookIdAndQuantityDTO> books = orderForm.books();

			for (BookIdAndQuantityDTO book : books) {
				cartBookService.deleteCartBook(memberId, book.id());
			}
		}
	}

}
