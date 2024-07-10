package store.novabook.store.search.document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import store.novabook.store.book.entity.Book;
import store.novabook.store.category.entity.Category;
import store.novabook.store.image.entity.Image;
import store.novabook.store.tag.entity.Tag;

@Getter
@Document(indexName = "supernova_search")
public class BookDocument {
	@Id
	@Field(type = FieldType.Long)
	private final Long id;

	@Field(type = FieldType.Keyword, analyzer = "korean")
	private final String title;

	@Field(type = FieldType.Keyword, analyzer = "korean")
	private final String author;

	@Field(type = FieldType.Keyword, analyzer = "korean")
	private final String publisher;

	@Field(type = FieldType.Text)
	private final String image;

	@Field(type = FieldType.Long)
	private final Long price;

	@Field(type = FieldType.Keyword)
	private final Long discountPrice;

	@Field(type = FieldType.Keyword)
	private final Integer score;

	@Field(type = FieldType.Boolean)
	private final Boolean isPackaged;

	@Field(type = FieldType.Keyword)
	private final Integer review;

	@Field(type = FieldType.Keyword, analyzer = "korean")
	private final List<String> categoryList;

	@Field(type = FieldType.Keyword, analyzer = "korean")
	private final List<String> tagList;

	@Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
	private final LocalDateTime createdAt;

	@Builder
	public BookDocument(Long id, String title, String author, String publisher, String image, Long price, Long discountPrice, Integer score, Boolean isPackaged, Integer review, List<String> tagList,
		List<String> categoryList) {
		this.id = id;
		this.title = title;
		this.author = author;
		this.publisher = publisher;
		this.image = image;
		this.price = price;
		this.discountPrice = discountPrice;
		this.score = score;
		this.isPackaged = isPackaged;
		this.review = review;
		this.tagList = tagList;
		this.categoryList = categoryList;
		this.createdAt = LocalDateTime.now();
	}

	public static BookDocument of(Book book, Image image, List<Tag> tags, List<Category> categories, Integer score, Integer review) {
		return BookDocument.builder()
			.id(book.getId())
			.title(book.getTitle())
			.author(book.getAuthor())
			.publisher(book.getPublisher())
			.image(image.getSource())
			.price(book.getPrice())
			.discountPrice(book.getDiscountPrice())
			.score(score)
			.isPackaged(book.isPackaged())
			.review(review)
			.tagList(tagNames(tags))
			.categoryList(categoryNames(categories))
			.build();
	}

	public static List<String> categoryNames(List<Category> categories) {
		Set<String> categoryNames = new HashSet<>();
		for (Category category : categories) {
			if (category.hasTopCategory()) {

				categoryNames.add(category.getTopCategory().getName());
				categoryNames.add(category.getName());

			} else {
				categoryNames.add(category.getName());

			}
		}

		return categoryNames.stream().toList();
	}

	public static List<String> tagNames(List<Tag> tags) {
		List<String> tagNames = new ArrayList<>();
		for (Tag tag : tags) {
			tagNames.add(tag.getName());
		}

		return tagNames;
	}
}
