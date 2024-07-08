package store.novabook.store.cart.dto.request;

import jakarta.validation.constraints.NotNull;

public record UpdateCartBookQuantityRequest (

	@NotNull(message = "bookId값이 null입니다.")
	Long bookId,
	@NotNull(message = "quantity값이 null입니다.")
	Integer quantity){
}
