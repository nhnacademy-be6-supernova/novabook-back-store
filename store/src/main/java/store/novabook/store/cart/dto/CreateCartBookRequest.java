package store.novabook.store.cart.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import store.novabook.store.book.entity.Book;
import store.novabook.store.cart.entity.Cart;

public record CreateCartBookRequest(
	//카트 안에 필요한 값만 쪼개서 가져옴
	//response, fromEntity
	@NotNull
	Cart cart,
	@NotNull
	Book book,
	@NotNull
	@Positive
	int quantity) {
}
