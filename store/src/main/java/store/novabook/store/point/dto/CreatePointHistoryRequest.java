package store.novabook.store.point.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

@Builder
public record CreatePointHistoryRequest(
	@NotNull
	Long ordersId,
	@NotNull
	Long pointPolicyId,
	@NotNull
	Long memberId,
	@NotNull
	String pointContent,
	@PositiveOrZero
	long pointAmount) {
}
