package store.novabook.store.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateMemberPasswordRequest(

	@Pattern(regexp = "^(?=.*\\d)(?=.*[a-zA-Z])(?=.*[~!@#$%^&*]).{10,16}$")
	@NotBlank(message = "비밀번호는 필수 입력 값입니다.")
	@Size(max = 16, message = "최대 16자까지 가능합니다.")
	String loginPassword,

	@Pattern(regexp = "^(?=.*\\d)(?=.*[a-zA-Z])(?=.*[~!@#$%^&*]).{10,16}$")
	@NotBlank(message = "비밀번호는 필수 입력 값입니다.")
	@Size(max = 16, message = "최대 16자까지 가능합니다.")
	String loginPasswordConfirm) {
}
