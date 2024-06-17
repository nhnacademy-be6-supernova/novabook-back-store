package store.novabook.store.order.entity;

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
import store.novabook.store.book.entity.Book;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity

public class OrdersBook {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "orders_id")
	private Orders orders;

	@ManyToOne
	@JoinColumn(name = "book_id")
	private Book book;

	@NotNull
	private int quantity;

	@NotNull
	private BigDecimal price;

	@NotNull
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

}
