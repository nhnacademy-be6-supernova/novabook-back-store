package store.novabook.store.tag.dto.request;

import jakarta.validation.constraints.NotNull;

public record UpdateTagRequest(
	@NotNull(message = "태그 이름값은 필수 입력값입니다.")
	String name) {
}
