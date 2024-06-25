package store.novabook.store.cart.dto;

import store.novabook.store.user.member.entity.Member;

public record CreateCartResponse(
	Member member,
	Boolean isExposed) {
	public static CreateCartResponse from(Member member, boolean isExposed) {
		return new CreateCartResponse(member, isExposed);
	}
}
