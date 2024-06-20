package store.novabook.store.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateOrdersStatusRequest(
	@NotNull(message = "이름은 필수 값입니다 ")
	@NotBlank(message = "값이 비어 있습니다")
	String name
){

}
