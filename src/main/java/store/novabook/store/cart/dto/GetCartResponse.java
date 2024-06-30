package store.novabook.store.cart.dto;

import java.util.List;

import lombok.Builder;
import store.novabook.store.user.member.entity.Member;

@Builder
public record GetCartResponse(
	List<Long> cartId) {
}
