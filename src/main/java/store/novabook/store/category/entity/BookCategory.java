package store.novabook.store.category.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.novabook.store.book.entity.Book;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class BookCategory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	@ManyToOne
	@JoinColumn(name = "book_id")
	private Book book;

	@NotNull
	private int depth;

	@NotNull
	private LocalDateTime createdAt;



	@Null
	private LocalDateTime updatedAt;

	public BookCategory(Book book, Category category, int depth) {
		this.category = category;
		this.book = book;
		this.depth = depth;
		this.createdAt = LocalDateTime.now();
	}
}
