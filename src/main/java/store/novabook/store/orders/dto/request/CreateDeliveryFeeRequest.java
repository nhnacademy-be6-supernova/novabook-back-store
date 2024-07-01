package store.novabook.store.orders.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateDeliveryFeeRequest(
	@NotNull(message = "fee값은 필수 입니다 ")
	Long fee) {
}
