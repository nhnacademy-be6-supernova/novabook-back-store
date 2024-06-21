package store.novabook.store.orders.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CreateReturnPolicyRequest(
	@NotBlank(message = "content 값이 비어 있습니다")
	String content
) {
}
