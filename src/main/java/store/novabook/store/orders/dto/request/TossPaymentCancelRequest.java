package store.novabook.store.orders.dto.request;

import lombok.Builder;

@Builder
public record TossPaymentCancelRequest(
	String paymentKey,
	String cancelReason
) {
}
