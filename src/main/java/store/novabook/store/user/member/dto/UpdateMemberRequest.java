package store.novabook.store.user.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateMemberRequest(

	@NotBlank(message = "비밀번호는 필수 입력 값입니다.")
	@Size(max = 20, message = "최대 20자까지 가능합니다.")
	String loginPassword,

	@NotBlank(message = "이름은 필수 입력 값입니다.")
	@Size(max = 50, message = "최대 50자까지 가능합니다.")
	String name,

	@NotBlank(message = "연락처는 필수 입력 값입니다.")
	@Size(max = 20, message = "최대 20자까지 가능합니다.")
	String number,

	@NotBlank(message = "이메일은 필수 입력 값입니다.")
	@Size(max = 100, message = "최대 100자까지 가능합니다.")
	String email
) {
}
