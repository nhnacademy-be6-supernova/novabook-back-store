package store.novabook.store.orders.dto.request;

import org.hibernate.validator.constraints.Range;

import jakarta.validation.constraints.NotNull;

public record BookIdAndQuantityDTO(
	@NotNull(message = "도서 ID는 null이 될 수 없습니다.")
	Long id,
	@Range(min = 1, max = 100_000, message = "도서 수량은 최대 100000 까지입니다.")
	long quantity
) {}
