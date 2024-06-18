package store.novabook.store.book.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import store.novabook.store.book.entity.Book;

@Builder
public record MiniBookResponse(
	Long id,
	Long bookStatusId,
	String isbn,
	String title,
	String subTitle,
	String engTitle,
	String index,
	String explanation,
	String translator,
	String publisher,
	LocalDateTime publicationDate,
	int inventory,
	long price,
	boolean isPackaged,
	String image,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {
	public static MiniBookResponse from(Book book) {
		return MiniBookResponse.builder()
			.id(book.getId())
			.bookStatusId(book.getBookStatus().getId())
			.isbn(book.getIsbn())
			.title(book.getTitle())
			.subTitle(book.getSubTitle())
			.engTitle(book.getEngTitle())
			.index(book.getIndex())
			.explanation(book.getExplanation())
			.translator(book.getTranslator())
			.publisher(book.getPublisher())
			.publicationDate(book.getPublicationDate())
			.inventory(book.getInventory())
			.price(book.getPrice())
			.isPackaged(book.isPackaged())
			.image(book.getImage())
			.createdAt(book.getCreatedAt())
			.updatedAt(book.getUpdatedAt())
			.build();
	}
}