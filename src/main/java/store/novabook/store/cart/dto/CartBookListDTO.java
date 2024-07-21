package store.novabook.store.cart.dto;

import java.util.List;

import lombok.Getter;

@Getter
public class CartBookListDTO {
	List<CartBookDTO> cartBookList;

	public CartBookListDTO(List<CartBookDTO> cartBookList) {
		this.cartBookList = cartBookList;
	}

	public CartBookListDTO() {
	}
}
