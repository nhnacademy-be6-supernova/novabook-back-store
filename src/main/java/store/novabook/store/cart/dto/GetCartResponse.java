package store.novabook.store.cart.dto;

import lombok.Builder;
import store.novabook.store.user.member.entity.Member;

@Builder
public record GetCartResponse(
	Member member,
	Boolean isExposed) {
}
