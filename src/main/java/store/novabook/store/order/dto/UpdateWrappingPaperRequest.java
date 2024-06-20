package store.novabook.store.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateWrappingPaperRequest(
	@NotNull(message = "price 값이 없습니다.")
	long price,
	@NotNull(message = "status 값이 없습니다.")
	@NotBlank(message = "status 값이 비었습니다.")
	String status
) {
}
