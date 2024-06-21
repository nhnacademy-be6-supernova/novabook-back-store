package store.novabook.store.orders.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateWrappingPaperRequest(
	@NotNull(message = "price 값이 없습니다.")
	@Min(value = 0, message = "0보다 커야 합니다 ")
	Long price,
	@NotBlank(message = "name 값이 없습니다.")
	String name,
	@NotBlank(message = "status 값이 비었습니다.")
	String status
) {
}
