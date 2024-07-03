package store.novabook.store.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record DuplicateEmailRequest(
	@NotBlank(message = "이메일은 필수 입력값입니다.")
	String email) {
}
