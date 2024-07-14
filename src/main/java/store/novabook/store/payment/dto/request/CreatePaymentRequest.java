package store.novabook.store.payment.dto.request;

import lombok.Builder;

@Builder
public record CreatePaymentRequest(
	String provider,
	String paymentKey
) {
}