package store.novabook.store.cart.dto;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Builder;
import store.novabook.store.cart.entity.CartBook;
import store.novabook.store.image.entity.Image;

@Builder
public record CartBookDTO(
	Long bookId,
	String title,
	String image,
	Long price,
	Long discountPrice,
	Integer quantity,
	boolean isPackaged,
	Long bookStatusId

) {
	public static CartBookDTO fromEntity(CartBook cartBook, Image image) {
		return CartBookDTO.builder()
			.bookId(cartBook.getBook().getId())
			.image(image.getSource())
			.title(cartBook.getBook().getTitle())
			.price(cartBook.getBook().getPrice())
			.discountPrice(cartBook.getBook().getDiscountPrice())
			.quantity(cartBook.getQuantity())
			.bookStatusId(cartBook.getBook().getBookStatus().getId())
			.isPackaged(cartBook.getBook().isPackaged())
			.build();
	}
}
