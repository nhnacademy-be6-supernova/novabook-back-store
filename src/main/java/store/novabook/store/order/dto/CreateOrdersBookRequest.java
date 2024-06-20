package store.novabook.store.order.dto;

import jakarta.validation.constraints.NotNull;

public record CreateOrdersBookRequest(
	@NotNull
	Long ordersId,
	@NotNull
	Long bookId,
	@NotNull
	int quantity,
	@NotNull
	long price
) {
}
