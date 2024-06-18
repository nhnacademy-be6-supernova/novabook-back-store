package store.novabook.store.point.dto;

import jakarta.validation.constraints.PositiveOrZero;
import store.novabook.store.order.entity.Orders;
import store.novabook.store.point.entity.PointPolicy;
import store.novabook.store.user.member.entity.Member;

public record CreatePointHistoryRequest(
	Orders orders,
	PointPolicy pointPolicy,
	Member member,
	String pointContent,
	@PositiveOrZero
	long pointAmount) {
}
