package store.novabook.store.cart.dto;

import lombok.Builder;
import store.novabook.store.member.entity.Member;

@Builder
public record GetCartResponse(
	Member member,
	Boolean isExposed) {
}
