package store.novabook.store.tag.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.novabook.store.book.entity.Book;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class BookTag {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "book_id")
	private Book book;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "tag_id")
	private Tag tag;

	@NotNull
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	public BookTag(Book book, Tag tag) {
		this.book = book;
		this.tag = tag;
		this.createdAt = LocalDateTime.now();
	}
}
