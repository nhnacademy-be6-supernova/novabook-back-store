package store.novabook.store.book.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import store.novabook.store.book.entity.Author;
import store.novabook.store.book.entity.AuthorBook;
import store.novabook.store.book.entity.Book;
import store.novabook.store.book.entity.BookDiscountRate;
import store.novabook.store.book.entity.BookStatus;
import store.novabook.store.book.entity.BookTag;
import store.novabook.store.book.entity.Likes;
import store.novabook.store.book.entity.Tag;
import store.novabook.store.category.entity.BookCategory;
import store.novabook.store.category.entity.Category;

@Builder
public record GetBookResponse(Long id,
							  String bookStatus,
							  String isbn,
							  String title,
							  String subTitle,
							  String engTitle,
							  String index,
							  String explanation,
							  String author,
							  String translator,
							  String publisher,
							  LocalDateTime publicationDate,
							  int inventory,
							  Long price,
							  boolean isPackaged,
							  String image,
							  List<String> tags,
							  List<String> categories,
							  int likes,
							  Long discount) {
	//description 필요?
	public static GetBookResponse fromEntity(
		Book book,
		List<AuthorBook> authorBooks, //작가 번역가 모두 들어있음
		BookDiscountRate discountRate,
		List<BookTag> tags,
		List<BookCategory> categories,
		int likes) {
		return GetBookResponse.builder()
			.id(book.getId())
			.bookStatus(book.getBookStatus().getName())
			.title(book.getTitle())
			.subTitle(book.getSubTitle())
			.engTitle(book.getEngTitle())
			.index(book.getIndex())
			.explanation(book.getExplanation())
			.author(getAuthorBookName(authorBooks, "작가"))
			.translator(getAuthorBookName(authorBooks, "번역가"))
			.publisher(book.getPublisher())
			.publicationDate(book.getPublicationDate())
			.inventory(book.getInventory())
			.price(book.getPrice())
			.isPackaged(book.isPackaged())
			.image(book.getImage())
			.tags(tagNames(tags))
			.categories(categoryNames(categories))
			.likes(likes)
			.discount(discountRate.getRate())
			.build();
	}

	public static String getAuthorBookName(List<AuthorBook> authorBooks, String roleName) {
		for (AuthorBook authorBook : authorBooks) {
			String role = authorBook.getAuthor().getRole();
			if (role.equals(roleName)) {
				return authorBook.getAuthor().getName();
			}
		}
		return null;
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
