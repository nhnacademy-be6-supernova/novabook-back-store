package store.novabook.store.user.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreateMemberRequest(

	@NotNull(message = "아이디는 필수 입력 값입니다.")
	@Size(max = 20, message = "최대 20자까지 가능합니다.")
	String loginId,

	@NotNull(message = "비밀번호는 필수 입력 값입니다.")
	@Size(max = 50, message = "최대 50자까지 가능합니다.")
	String loginPassword,

	@NotNull(message = "이름은 필수 입력 값입니다.")
	@Size(max = 50, message = "최대 50자까지 가능합니다.")
	String name,

	@NotNull(message = "연락처는 필수 입력 값입니다.")
	@Size(max = 20, message = "최대 20자까지 가능합니다.")
	String number,

	@NotNull(message = "이메일은 필수 입력 값입니다.")
	@Email
	@Size(max = 100, message = "최대 100자까지 가능합니다.")
	String email,

	@NotNull(message = "생년월일은 필수 입력 값입니다.")
	Integer birthYear,

	@NotNull(message = "생년월일은 필수 입력 값입니다.")
	Integer birthMonth,

	@NotNull(message = "생년월일은 필수 입력값입니다.")
	Integer birthDay) {

}
