package store.novabook.store.payment.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import store.novabook.store.payment.entity.Payment;

@Builder
public record GetPaymentResponse(
	Long id,
	Long ordersId,
	String provider,
	String paymentKey,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {
	public static GetPaymentResponse from(Payment payment) {
		return GetPaymentResponse.builder()
			.id(payment.getId())
			.ordersId(payment.getOrdersId())
			.provider(payment.getProvider())
			.paymentKey(payment.getPaymentKey())
			.createdAt(payment.getCreatedAt())
			.updatedAt(payment.getUpdatedAt())
			.build();
	}
}