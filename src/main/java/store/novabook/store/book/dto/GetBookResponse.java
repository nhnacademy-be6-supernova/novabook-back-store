package store.novabook.store.book.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import store.novabook.store.book.entity.Book;
import store.novabook.store.tag.entity.BookTag;
import store.novabook.store.category.entity.BookCategory;

@Builder
public record GetBookResponse(Long id,
							  Long bookStatusId,
							  String isbn,
							  String title,
							  String bookIndex,
							  String description,
							  String descriptionDetail,
							  String author,
							  String publisher,
							  LocalDateTime publicationDate,
							  int inventory,
							  Long price,
							  Long discountPrice,
							  boolean isPackaged,
							  String image,
							  List<String> tags,
							  List<String> categories,
							  int likes
							  ) {
	//description 필요?
	public static GetBookResponse fromEntity(
		Book book,
		List<BookTag> tags,
		List<BookCategory> categories,
		int likes) {
		return GetBookResponse.builder()
			.id(book.getId())
			.isbn(book.getIsbn())
			.bookStatusId(book.getBookStatus().getId())
			.title(book.getTitle())
			.description(book.getDescription())
			.descriptionDetail(book.getDescriptionDetail())
			.bookIndex(book.getBookIndex())
			.author(book.getAuthor())
			.publisher(book.getPublisher())
			.publicationDate(book.getPublicationDate())
			.inventory(book.getInventory())
			.price(book.getPrice())
			.isPackaged(book.isPackaged())
			.image(book.getImage())
			.tags(tagNames(tags))
			.categories(categoryNames(categories))
			.likes(likes)
			.discountPrice(book.getDiscountPrice())
			.build();
	}


	public static List<String> tagNames(List<BookTag> bookTags) {
		List<String> tagNames = new ArrayList<>();
		for (BookTag tag : bookTags) {
			String tagName = tag.getTag().getName();
			if (tagName != null && !tagName.isEmpty()) {
				tagNames.add(tagName);
			}
		}
		return tagNames;
	}

	public static List<String> categoryNames(List<BookCategory> categories) {
		List<String> categoryNames = new ArrayList<>();
		for (BookCategory category : categories) {
			String categoryName = category.getCategory().getName();
			if (categoryName != null && !categoryName.isEmpty()) {
				categoryNames.add(categoryName);
			}
		}
		return categoryNames;
	}

}
