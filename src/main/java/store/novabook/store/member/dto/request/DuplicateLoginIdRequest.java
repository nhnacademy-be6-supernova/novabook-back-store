package store.novabook.store.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record DuplicateLoginIdRequest(
	@NotBlank(message = "로그인 아이디는 필수 입력값입니다.")
	String loginId) {
}
