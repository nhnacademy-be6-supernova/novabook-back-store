package store.novabook.store.cart.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateCartRequest(
	@NotNull
	Long memberId,
	@NotNull
	Boolean isExposed) {
}
