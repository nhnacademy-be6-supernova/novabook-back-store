package store.novabook.store.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateWrappingPaperRequest(
	@NotNull(message = "fee 값은 필수 입니다 ")
	long price,
	@NotNull(message = "name 값은 필수 입니다 ")
	@NotBlank(message = "name 값이 비어있습니다 ")
	String name,
	@NotNull(message = "status 값은 필수 입니다 ")
	@NotBlank(message = "status 값이 비어있습니다 ")
	String status) {
}
