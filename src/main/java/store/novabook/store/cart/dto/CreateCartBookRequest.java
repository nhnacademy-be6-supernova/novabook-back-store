package store.novabook.store.cart.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record CreateCartBookRequest(
	@NotNull
	Long cartId,
	@NotNull
	Long bookId,
	@NotNull
	@Positive
	int quantity) {
}
