package store.novabook.store.cart.entity;

import static jakarta.persistence.GenerationType.*;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.novabook.store.book.entity.Book;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@EntityListeners(AuditingEntityListener.class)
public class CartBook {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "cart_id")
	private Cart cart;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "book_id")
	private Book book;

	@NotNull
	private int quantity;

	@NotNull
	@Column(name = "is_exposed")
	private boolean isExposed;

	@NotNull
	@CreatedDate
	private LocalDateTime createdAt;

	@LastModifiedDate
	private LocalDateTime updatedAt;

	@Builder
	public CartBook(Cart cart, Book book, int quantity) {
		this.cart = cart;
		this.book = book;
		this.quantity = quantity;
		this.isExposed = true;
	}

	public static CartBook of(Cart cart, Book book, int quantity) {
		return CartBook.builder().cart(cart).book(book).quantity(quantity).build();
	}

	public void updateIsExposed(boolean isExposed) {
		this.isExposed = isExposed;
	}

	public void updateQuantity(int quantity) {
		this.quantity = quantity;
	}

}
