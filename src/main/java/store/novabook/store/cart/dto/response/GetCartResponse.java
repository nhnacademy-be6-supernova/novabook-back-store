package store.novabook.store.cart.dto.response;

import java.util.List;

import lombok.Builder;

@Builder
public record GetCartResponse(
	List<Long> cartId) {
}
