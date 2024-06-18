package store.novabook.store.point.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record CreatePointPolicyRequest(
	@NotNull
	@PositiveOrZero
	Long reviewPointRate,
	@NotNull
	@PositiveOrZero
	Long basicPoint,
	@NotNull
	@PositiveOrZero
	Long registerPoint) {
}
