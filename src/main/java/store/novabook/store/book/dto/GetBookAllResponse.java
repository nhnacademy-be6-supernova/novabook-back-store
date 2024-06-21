package store.novabook.store.book.dto;

import lombok.Builder;
import store.novabook.store.book.entity.Book;

@Builder
public record GetBookAllResponse(
	Long id,
	Long bookStatusId,
	String title,
	String bookIndex,
	int inventory,
	Long price,
	Long discountPrice,
	boolean isPackaged) {
	public static GetBookAllResponse fromEntity(Book book) {
		return GetBookAllResponse.builder()
			.id(book.getId())
			.bookStatusId(book.getBookStatus().getId())
			.title(book.getTitle())
			.bookIndex(book.getBookIndex())
			.inventory(book.getInventory())
			.price(book.getPrice())
			.discountPrice(book.getDiscountPrice())
			.isPackaged(book.isPackaged())
			.build();
	}

}




