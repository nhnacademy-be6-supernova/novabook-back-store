package store.novabook.store.cart.dto.response;

import jakarta.validation.constraints.NotNull;

public record CreateCartBookResponse(
	@NotNull
	Long id) {
}
