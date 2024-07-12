package store.novabook.store.orders.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import store.novabook.store.orders.entity.Orders;

@Builder
public record GetOrdersAdminResponse(
	Long ordersId,
	String memberLoginId,
	Long ordersStatusId,
	LocalDateTime ordersDate,
	// Long discountAmount,
	Long totalAmount,
	LocalDateTime createdAt
) {
	public static GetOrdersAdminResponse from(Orders orders) {
		return GetOrdersAdminResponse.builder()
			.ordersId(orders.getId())
			.memberLoginId(orders.getMember() == null ? "비회원" : orders.getMember().getLoginId())
			.ordersStatusId(orders.getOrdersStatus().getId())
			.ordersDate(orders.getOrdersDate())
			.totalAmount(orders.getTotalAmount())
			// .discountAmount(orders.getDiscountAmount())
			.createdAt(orders.getCreatedAt())
			.build();
	}
}
