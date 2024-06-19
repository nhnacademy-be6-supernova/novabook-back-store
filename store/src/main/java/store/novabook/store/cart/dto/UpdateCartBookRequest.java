package store.novabook.store.cart.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record UpdateCartBookRequest(
	@NotNull
	Long cartBookId,
	@NotNull
	Long cartId,
	@NotNull
	Long bookId,
	@NotNull
	@Positive
	int quantity) {
}
