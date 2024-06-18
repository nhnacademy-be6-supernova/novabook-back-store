package store.novabook.store.point.dto;

import store.novabook.store.order.entity.Orders;
import store.novabook.store.point.entity.PointPolicy;
import store.novabook.store.user.member.entity.Member;

public record GetPointHistoryResponse(Orders orders,
									  PointPolicy pointPolicy,
									  Member member,
									  String pointContent,
									  long pointAmount) {
}
