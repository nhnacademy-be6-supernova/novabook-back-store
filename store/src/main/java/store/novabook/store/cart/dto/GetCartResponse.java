package store.novabook.store.cart.dto;

import store.novabook.store.user.member.entity.Users;

public record GetCartResponse(Users users,
							  boolean isExposed) {
}
