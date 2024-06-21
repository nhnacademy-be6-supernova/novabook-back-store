package store.novabook.store.orders.entity;

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
import store.novabook.store.orders.dto.CreateOrdersBookRequest;
import store.novabook.store.orders.dto.UpdateOrdersBookRequest;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity

public class OrdersBook {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "orders_id")
	private Orders orders;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "book_id")
	private Book book;

	@NotNull
	private int quantity;

	@NotNull
	private long price;

	@NotNull
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	public OrdersBook(Orders orders, Book book, CreateOrdersBookRequest request) {
		this.orders = orders;
		this.book = book;
		this.quantity = request.quantity();
		this.price = request.price();
		this.createdAt = LocalDateTime.now();
	}

	public void update(Orders orders, Book book, UpdateOrdersBookRequest request) {

	}
}
