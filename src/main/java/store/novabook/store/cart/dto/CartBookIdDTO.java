package store.novabook.store.cart.dto;

import java.util.Map;

import jakarta.validation.constraints.NotNull;

public record CartBookIdDTO(
	@NotNull(message = "bookids값이 null입니다.")
	Map<Long, Integer> bookIdsAndQuantity) {
}
