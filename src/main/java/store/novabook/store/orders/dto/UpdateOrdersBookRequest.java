package store.novabook.store.orders.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateOrdersBookRequest(
	@NotNull(message = "ordersId은 필수 값입니다 ")
	Long ordersId,
	@NotNull(message = "bookId은 필수 값입니다 ")
	Long bookId,
	@NotNull(message = "quantity은 필수 값입니다 ")
	@Min(value = 0, message = "0보다 커야 합니다 ")
	Integer quantity,
	@NotNull(message = "price은 필수 값입니다 ")
	@Min(value = 0, message = "0보다 커야 합니다 ")
	Long price
) {
}
