package store.novabook.store.image.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CreateImageRequest(
	@NotBlank
	String source
) {
}
