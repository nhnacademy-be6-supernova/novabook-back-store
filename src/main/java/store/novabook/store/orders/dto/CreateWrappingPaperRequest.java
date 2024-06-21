package store.novabook.store.orders.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateWrappingPaperRequest(
	@NotNull(message = "fee 값은 필수 입니다 ")
	long price,
	@NotBlank(message = "name 값이 비어있습니다 ")
	String name,
	@NotBlank(message = "status 값이 비어있습니다 ")
	String status) {
}
