package store.novabook.store.cart.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record CreateCartBookRequest(
	//카트 안에 필요한 값만 쪼개서 가져옴
	//response, fromEntity
	@NotNull
	Long cartId,
	@NotNull
	Long bookId,
	@NotNull
	@Positive
	int quantity) {
}
