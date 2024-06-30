package store.novabook.store.user.member.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateMemberPasswordRequest(

	@NotBlank(message = "비밀번호는 필수 입력 값입니다.") String loginPassword,

	@NotBlank(message = "비밀번호는 필수 입력 값입니다.") String loginPasswordConfirm) {
}
