package store.novabook.store.cart.dto;

import lombok.Builder;
import store.novabook.store.cart.entity.CartBook;

@Builder
public record GetCartBookResponse(
	Long id,
	Long bookId,
	Integer quantity) {
	public static GetCartBookResponse fromEntity(CartBook cartBook) {
		return GetCartBookResponse.builder()
			.bookId(cartBook.getBook().getId())
			.quantity(cartBook.getQuantity())
			.build();
	}
}
