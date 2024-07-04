package store.novabook.store.cart.dto.response;

import lombok.Builder;
import store.novabook.store.cart.entity.CartBook;
import store.novabook.store.image.entity.Image;

@Builder
public record GetCartBookResponse(
	Long cartBookId,
	String image,
	String title,
	Long price,
	Long discountPrice,
	Integer quantity
) {
	public static GetCartBookResponse fromEntity(CartBook cartBook, Image image) {
		return GetCartBookResponse.builder()
			.cartBookId(cartBook.getId())
			.image(image.getSource())
			.title(cartBook.getBook().getTitle())
			.price(cartBook.getBook().getPrice())
			.discountPrice(cartBook.getBook().getDiscountPrice())
			.quantity(cartBook.getQuantity())
			.build();
	}
}
