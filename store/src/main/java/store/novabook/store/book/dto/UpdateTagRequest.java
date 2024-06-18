package store.novabook.store.book.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UpdateTagRequest(
	@NotNull(message = "태그 아이디값은 필수 입력값입니다.")
	Long id,
	@NotNull(message = "태그 이름값은 필수 입력값입니다.")
	String name) {
}
