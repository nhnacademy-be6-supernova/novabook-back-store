package store.novabook.store.book.entity;

import java.time.LocalDateTime;

import org.hibernate.sql.Update;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.novabook.store.book.dto.CreateBookRequest;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "book_status_id")
	private BookStatus bookStatus;

	@NotNull
	private String isbn;

	@NotNull
	private String title;


	@NotNull
	private String index;

	@NotNull
	private String description;

	@NotNull
	private String descriptionDetail;

	@NotNull
	private String author;

	@NotNull
	private String translator;

	@NotNull
	private String publisher;


	@NotNull
	private LocalDateTime publicationDate;

	@NotNull
	int inventory;

	@NotNull
	private long price;

	@NotNull
	boolean isPackaged;

	@NotNull
	private String image;

	@NotNull
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;


	@Builder
	public Book(BookStatus bookStatus,
				String isbn,
				String title,
				String index,
				String description,
				String descriptionDetail,
				String author,
				String publisher,
				LocalDateTime publicationDate,
				int inventory,
				Long price,
				boolean isPackaged,
				String image) {
		this.bookStatus = bookStatus;
		this.isbn = isbn;
		this.title = title;
		this.index = index;
		this.description = description;
		this.descriptionDetail = descriptionDetail;
		this.author = author;
		this.publisher = publisher;
		this.publicationDate = publicationDate;
		this.inventory = inventory;
		this.price = price;
		this.isPackaged = isPackaged;
		this.image = image;
		this.createdAt = LocalDateTime.now();

	}


	public static Book of(CreateBookRequest request) {
		return Book.builder()
			.bookStatus(request.bookStatus())
			.isbn(request.isbn())
			.title(request.title())
			.index(request.index())
			.description(request.description())
			.descriptionDetail(request.descriptionDetail())
			.author(request.author())
			.publisher(request.publisher())
			.publicationDate(request.publicationDate())
			.inventory(request.inventory())
			.price(request.price())
			.isPackaged(request.isPackaged())
			.image(request.image())
			.build();
	}

	public void update() {

	}

}
