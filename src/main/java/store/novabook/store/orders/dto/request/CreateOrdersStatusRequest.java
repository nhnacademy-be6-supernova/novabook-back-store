package store.novabook.store.orders.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CreateOrdersStatusRequest(
	@NotBlank(message = "값이 비어 있습니다")
	String name
) {

}
