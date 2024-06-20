package store.novabook.store.cart.dto;

import lombok.Builder;
import store.novabook.store.user.member.entity.Users;

@Builder
public record CreateCartResponse(Users users,
								 boolean isExposed) {
}
