package store.novabook.store.book.dto;

import lombok.Builder;
import store.novabook.store.book.entity.Book;
@Builder
public record UpdateBookRequest(Long id,
								Long bookStatusId,
								String bookIndex,
								int inventory,
								Long price,
								Long discountPrice,
								boolean isPackaged) {

	public UpdateBookRequest fromEntity(Book book) {
		return UpdateBookRequest.builder()
			.id(book.getId())
			.bookStatusId(book.getBookStatus().getId())
			.bookIndex(book.getBookIndex())
			.inventory(book.getInventory())
			.price(book.getPrice())
			.discountPrice(book.getDiscountPrice())
			.isPackaged(book.isPackaged())
			.build();

	}
}
