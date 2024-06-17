package store.novabook.store.book.entity;

import java.math.BigDecimal;
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

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	private Long id;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "book_status_id")
	private BookStatus bookStatus;

	@NotNull
	private String isbn;

	@NotNull
	private String title;

	private String subTitle;

	private String engTitle;

	@NotNull
	private String index;

	@NotNull
	private String explanation;

	@NotNull
	private String translator;

	@NotNull
	private String publisher;

	@NotNull
	private LocalDateTime publicationDate;

	@NotNull
	int inventory;

	@NotNull
	private BigDecimal price;

	@NotNull
	boolean isPackaging;

	@NotNull
	private String image;

	@NotNull
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

}
