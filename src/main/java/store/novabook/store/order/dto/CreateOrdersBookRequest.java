package store.novabook.store.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateOrdersBookRequest(
	@NotNull(message = "ordersId 값은 필수 입니다")
	Long ordersId,
	@NotNull(message = "bookId 값은 필수 입니다")
	Long bookId,
	@NotNull(message = "quantity 값은 필수 입니다")
	@Min(value = 0,message = "0보다 커야 합니다 ")
	int quantity,
	@NotNull(message = "price 값은 필수 입니다")
	@Min(value = 0,message = "0보다 커야 합니다 ")
	long price
) {
}
