package store.novabook.store.order.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import store.novabook.store.order.entity.OrdersBook;
@Builder
public record GetOrdersBookResponse(
	Long id,
	Long ordersId,
	Long bookId,
	int quantity,
	long price,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {
	public static GetOrdersBookResponse from(OrdersBook ordersBook ) {
		return GetOrdersBookResponse.builder()
			.bookId(ordersBook.getBook().getId())
			.ordersId(ordersBook.getOrders().getId())
			.quantity(ordersBook.getQuantity())
			.price(ordersBook.getPrice())
			.createdAt(ordersBook.getCreatedAt())
			.updatedAt(ordersBook.getUpdatedAt())
			.build();
	}
}
