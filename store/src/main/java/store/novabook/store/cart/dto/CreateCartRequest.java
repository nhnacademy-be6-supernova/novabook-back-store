package store.novabook.store.cart.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import store.novabook.store.user.member.entity.Users;

@Builder
public record CreateCartRequest(
	@NotNull
	Users users,

	@NotNull
	boolean isExposed) {
}
