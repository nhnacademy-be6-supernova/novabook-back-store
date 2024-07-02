package store.novabook.store.orders.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import store.novabook.store.orders.entity.Orders;

@Builder
public record GetOrdersResponse(
	Long memberId,
	Long deliveryFeeId,
	Long wrappingPaperId,
	Long ordersStatusId,
	LocalDateTime ordersDate,
	Long totalAmount,
	LocalDateTime deliveryDate,
	long bookPurchaseAmount,
	String deliveryAddress,
	String receiverName,
	String receiverNumber,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {
	public static GetOrdersResponse form(Orders orders) {
		return GetOrdersResponse.builder()
			.memberId(orders.getId())
			.deliveryFeeId(orders.getDeliveryFee().getId())
			.wrappingPaperId(orders.getWrappingPaper().getId())
			.ordersStatusId(orders.getOrdersStatus().getId())
			.ordersDate(orders.getOrdersDate())
			.totalAmount(orders.getTotalAmount())
			.deliveryDate(orders.getDeliveryDate())
			.bookPurchaseAmount(orders.getBookPurchaseAmount())
			.deliveryAddress(orders.getDeliveryAddress())
			.receiverName(orders.getReceiverName())
			.receiverNumber(orders.getReceiverNumber())
			.createdAt(orders.getCreatedAt())
			.updatedAt(orders.getUpdatedAt())
			.build();
	}
}
