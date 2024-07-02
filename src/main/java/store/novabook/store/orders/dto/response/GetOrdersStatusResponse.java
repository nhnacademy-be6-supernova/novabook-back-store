package store.novabook.store.orders.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import store.novabook.store.orders.entity.OrdersStatus;

@Builder
public record GetOrdersStatusResponse(
	Long id,
	String name,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {
	public static GetOrdersStatusResponse form(OrdersStatus ordersStatus) {
		return GetOrdersStatusResponse.builder()
			.id(ordersStatus.getId())
			.name(ordersStatus.getName())
			.createdAt(ordersStatus.getCreatedAt())
			.updatedAt(ordersStatus.getUpdatedAt())
			.build();
	}
}
