package store.novabook.store.book.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import store.novabook.store.book.entity.Book;

@Builder
public record SearchBookResponse(
	Long id,
	Long bookStatusId,
	String isbn,
	String title,
	String index,
	String description,
	String descriptionDetail,
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
	public static SearchBookResponse from(Book book) {
		return SearchBookResponse.builder()
			.id(book.getId())
			.bookStatusId(book.getBookStatus().getId())
			.isbn(book.getIsbn())
			.title(book.getTitle())
			.description(book.getDescription())
			.descriptionDetail(book.getDescriptionDetail())
			.index(book.getIndex())
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