package store.novabook.store.cart.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateCartBookRequest(
	@NotNull
	Long cartId,
	@NotNull
	Long bookId,
	@NotNull
	@Positive
	Integer quantity
) {
}
