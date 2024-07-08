package store.novabook.store.cart.dto.response;

import java.util.List;

import jakarta.validation.constraints.NotNull;

public record CreateCartBookListResponse(
	@NotNull(message = "ids값이 null입니다.")
	List<Long> ids) {
}
