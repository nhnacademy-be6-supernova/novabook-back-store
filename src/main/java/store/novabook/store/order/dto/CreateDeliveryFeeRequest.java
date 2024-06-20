package store.novabook.store.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateDeliveryFeeRequest(
	@NotNull(message = "fee값은 필수 입니다 ")
	long fee) {
}
