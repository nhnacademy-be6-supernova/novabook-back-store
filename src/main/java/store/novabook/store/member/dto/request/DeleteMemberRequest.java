package store.novabook.store.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record DeleteMemberRequest(
	@NotBlank(message = "비밀번호는 필수 입력값 입니다.")
	String loginPassword) {
}
