package store.novabook.store.cart.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateCartBookRequest(
	@NotNull
	Long bookId,

	@NotEmpty
	String title,

	String image,

	@NotNull
	Integer price,

	@NotNull
	Integer discountPrice,

	@Positive
	Integer quantity
) {
}
