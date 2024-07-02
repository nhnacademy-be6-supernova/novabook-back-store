package store.novabook.store.cart.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateCartRequest(
	@NotNull
	Long memberId,
	@NotNull
	Boolean isExposed) {
}
