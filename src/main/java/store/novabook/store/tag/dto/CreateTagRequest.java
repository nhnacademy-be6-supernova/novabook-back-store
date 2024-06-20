package store.novabook.store.tag.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateTagRequest(
	@NotNull(message = "태그명은 필수 입력값입니다.")
	String name) {
}
