package store.novabook.store.cart.dto.response;

import java.util.List;

import lombok.Builder;

@Builder
public record GetCartResponse(
	Long cartId,
	List<GetCartBookResponse> cartBookList
	) {
}
