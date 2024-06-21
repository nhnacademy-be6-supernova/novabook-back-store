package store.novabook.store.orders.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UpdateOrdersStatusRequest(
	@NotBlank(message = "값이 비어 있습니다")
	String name
) {

}
