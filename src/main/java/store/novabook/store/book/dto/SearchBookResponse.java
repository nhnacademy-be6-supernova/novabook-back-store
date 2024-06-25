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
	String description,
	String descriptionDetail,
	String author,
	String publisher,
	int inventory,
	LocalDateTime publicationDate,
	Long price,
	Long discountPrice,
	boolean isPackaged,
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
			.author(book.getAuthor())
			.publisher(book.getPublisher())
			.inventory(book.getInventory())
			.price(book.getPrice())
			.isPackaged(book.isPackaged())
			.publicationDate(book.getPublicationDate())
			.createdAt(book.getCreatedAt())
			.updatedAt(book.getUpdatedAt())
			.build();
	}
}