package store.novabook.store.cart.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateCartBookRequest(
	@NotNull(message = "bookId가 null 값 입니다.")
	Long bookId,

	@NotNull(message = "수량이 없습니다.")
	@Positive(message = "수량은 음수값이 될 수 없습니다.")
	Integer quantity
) {
}
