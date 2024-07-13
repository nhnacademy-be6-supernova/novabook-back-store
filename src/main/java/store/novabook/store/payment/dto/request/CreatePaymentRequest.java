package store.novabook.store.payment.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreatePaymentRequest(
	String provider,
	String paymentKey
) {
}