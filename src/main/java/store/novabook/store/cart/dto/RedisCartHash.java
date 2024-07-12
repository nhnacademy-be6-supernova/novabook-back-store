package store.novabook.store.cart.dto;

import java.util.Collections;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Builder;
import lombok.Getter;

@RedisHash("cart")
@Getter
public class RedisCartHash {

	@Id
	Object cartId;

	List<CartBookDTO> cartBookList;

	@Builder
	public RedisCartHash(Object cartId, List<CartBookDTO> cartBookList) {
		this.cartId = cartId;
		this.cartBookList = cartBookList;
	}
	public static RedisCartHash of(Object cartId) {
		return RedisCartHash.builder()
			.cartId(cartId)
			.cartBookList(Collections.emptyList())
			.build();
	}

	public static RedisCartHash of(Object cartId, CartBookDTO request) {
		return RedisCartHash.builder()
			.cartId(cartId)
			.cartBookList(List.of(request))
			.build();
	}

	public static RedisCartHash of(Object cartId, CartBookListDTO request) {
		return RedisCartHash.builder()
			.cartId(cartId)
			.cartBookList(request.getCartBookList())
			.build();

	}

	public void update(List<CartBookDTO> newCartBookList){
		this.cartBookList = newCartBookList;
	}

}