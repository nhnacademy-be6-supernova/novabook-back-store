package store.novabook.store.orders.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import store.novabook.store.orders.entity.Orders;

@Builder
public record GetOrdersResponse(
	String code,
	Long memberId, //회원 id
	Long deliveryFeeId,
	Long wrappingPaperId,
	Long ordersStatusId,
	LocalDateTime ordersDate,
	Long totalAmount,
	LocalDateTime deliveryDate,
	long bookPurchaseAmount,
	String deliveryAddress,
	Long couponId,
	Long usePointAmount,
	Long pointSaveAmount,
	String paymentKey,
	String receiverName,
	String receiverNumber,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {
	public static GetOrdersResponse form(Orders orders) {
		Long memberId;

		if (orders.getMember() == null) {
			memberId = null;
		} else {
			memberId = orders.getMember().getId();
		}

		return GetOrdersResponse.builder()
			.code(orders.getCode())
			.memberId(memberId)
			.deliveryFeeId(orders.getDeliveryFee().getId())
			.wrappingPaperId(orders.getWrappingPaper().getId())
			.ordersStatusId(orders.getOrdersStatus().getId())
			.ordersDate(orders.getOrdersDate())
			.totalAmount(orders.getTotalAmount())
			.deliveryDate(orders.getDeliveryDate())
			.bookPurchaseAmount(orders.getBookPurchaseAmount())
			.deliveryAddress(orders.getDeliveryAddress())
			.couponId(orders.getUseCouponId())
			.usePointAmount(orders.getUsePointAmount())
			.pointSaveAmount(orders.getPointSaveAmount())
			.paymentKey(orders.getPayment().getPaymentKey())
			.receiverName(orders.getReceiverName())
			.receiverNumber(orders.getReceiverNumber())
			.createdAt(orders.getCreatedAt())
			.updatedAt(orders.getUpdatedAt())
			.build();
	}
}
