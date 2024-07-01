package store.novabook.store.search.document;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import store.novabook.store.book.entity.Book;

@Getter
@Document(indexName = "supernova")
public class BookDocument {
	@Id
	@Field(type = FieldType.Long)
	private Long id;

	@Field(type = FieldType.Text)
	private String title;

	@Field(type = FieldType.Text)
	private String author;

	@Field(type = FieldType.Text)
	private String publisher;

	@Field(type = FieldType.Text)
	private String image;

	@Field(type = FieldType.Text)
	private List<String> tagList;

	@Field(type = FieldType.Text)
	private List<String> categoryList;

	@Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
	private LocalDateTime createdAt;

	@Builder
	public BookDocument(Long id, String title, String author, String publisher) {
		this.id = id;
		this.title = title;
		this.author = author;
		this.publisher = publisher;
		this.createdAt = LocalDateTime.now();
	}

	public static BookDocument of(Book book) {
		return BookDocument.builder().id(book.getId()).title(book.getTitle()).author(book.getAuthor()).publisher(book.getPublisher()).build();
	}

}
