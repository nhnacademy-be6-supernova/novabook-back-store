package store.novabook.store.point.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import store.novabook.store.order.entity.Orders;
import store.novabook.store.point.entity.PointPolicy;
import store.novabook.store.user.member.entity.Member;

@Builder
public record CreatePointHistoryRequest(
	@NotNull
	Orders orders,
	@NotNull
	PointPolicy pointPolicy,
	@NotNull
	Member member,
	@NotNull
	String pointContent,
	@PositiveOrZero
	long pointAmount) {
}
