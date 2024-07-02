package store.novabook.store.cart.dto.response;

import store.novabook.store.member.entity.Member;

public record CreateCartResponse(
	Member member,
	Boolean isExposed) {
	public static CreateCartResponse from(Member member, boolean isExposed) {
		return new CreateCartResponse(member, isExposed);
	}
}
