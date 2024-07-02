package store.novabook.store.book.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import store.novabook.store.book.entity.Book;
import store.novabook.store.image.entity.Image;

@Builder
public record GetBookResponse(
	Long id,
	String isbn,
	String title,
	String description,
	String descriptionDetail,
	String author,
	String publisher,
	LocalDateTime publicationDate,
	int inventory,
	Long price,
	Long discountPrice,
	boolean isPackaged,
	List<String> tags,
	List<String> categories,
	int likes,
	int score,
	String image

) {
	public static GetBookResponse fromEntity(
		Book book,
		List<String> tags,
		List<String> categories,
		int likes,
		int score,
		Image image) {
		return GetBookResponse.builder()
			.id(book.getId())
			.isbn(book.getIsbn())
			.title(book.getTitle())
			.description(book.getDescription())
			.descriptionDetail(book.getDescriptionDetail())
			.author(book.getAuthor())
			.publisher(book.getPublisher())
			.publicationDate(book.getPublicationDate())
			.inventory(book.getInventory())
			.price(book.getPrice())
			.isPackaged(book.isPackaged())
			.tags(tags)
			.categories(categories)
			.likes(likes)
			.discountPrice(book.getDiscountPrice())
			.score(score)
			.image(image.getSource())
			.build();
	}
}
