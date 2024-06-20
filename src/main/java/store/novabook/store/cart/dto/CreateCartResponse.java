package store.novabook.store.cart.dto;

import store.novabook.store.user.member.entity.Users;

public record CreateCartResponse(
	Users users,
	boolean isExposed) {
	public static CreateCartResponse from(Users users, boolean isExposed) {
		return new CreateCartResponse(users, isExposed);
	}
}
