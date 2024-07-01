package store.novabook.store.payment.dto.response;

import lombok.Builder;
import store.novabook.store.payment.entity.Payment;

@Builder
public record CreatePaymentResponse(Long id) {
	public static CreatePaymentResponse fromEntity(Payment payment) {
		return CreatePaymentResponse.builder()
			.id(payment.getId())
			.build();
	}
}
