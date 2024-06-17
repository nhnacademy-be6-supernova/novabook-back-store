package store.novabook.store.point.dto;

import lombok.Builder;
import store.novabook.store.order.entity.Orders;
import store.novabook.store.point.entity.PointPolicy;

@Builder
public record PointHistoryCreateRequest(Orders orders,
										PointPolicy pointPolicy,
										String pointContent,
										long pointAmount) {
}
