package store.novabook.store.cart.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotNull;

public record DeleteCartBookListRequest(
	@NotNull(message = "bookids값이 null입니다.")
	List<Long> bookIds) {
}
