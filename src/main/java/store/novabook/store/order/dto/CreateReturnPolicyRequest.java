package store.novabook.store.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateReturnPolicyRequest(
	@NotBlank(message = "content 값이 비어 있습니다")
	String content
) {
}
