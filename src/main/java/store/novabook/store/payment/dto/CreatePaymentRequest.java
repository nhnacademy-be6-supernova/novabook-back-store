package store.novabook.store.payment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreatePaymentRequest(
	Long ordersId,
	@NotNull(message = "orderId 값은 필수 입니다 ")
	String provider,
	String paymentKey
) {
}