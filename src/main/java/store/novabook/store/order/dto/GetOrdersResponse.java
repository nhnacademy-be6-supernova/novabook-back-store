package store.novabook.store.order.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import store.novabook.store.order.entity.Orders;

@Builder
public record GetOrdersResponse(
	Long userId,
	Long deliveryFeeId,
	Long wrappingPaperId,
	Long ordersStatusId,
	Long returnPolicyId,
	LocalDateTime ordersDate,
	Long totalAmount,
	LocalDateTime deliveryDate,
	long bookPurchaseAmount,
	String deliveryAddress,
	String recieverName,
	String recieverNumber,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
){
	public static GetOrdersResponse form(Orders orders){
		return GetOrdersResponse.builder()
			.userId(orders.getId())
			.deliveryFeeId(orders.getDeliveryFee().getId())
			.wrappingPaperId(orders.getWrappingPaper().getId())
			.ordersStatusId(orders.getOrdersStatus().getId())
			.ordersDate(orders.getOrdersDate())
			.totalAmount(orders.getTotalAmount())
			.deliveryDate(orders.getDeliveryDate())
			.bookPurchaseAmount(orders.getBookPurchaseAmount())
			.deliveryAddress(orders.getDeliveryAddress())
			.recieverName(orders.getReceieverName())
			.recieverNumber(orders.getReceieverNumber())
			.createdAt(orders.getCreatedAt())
			.updatedAt(orders.getUpdatedAt())
			.build();
	}
}
