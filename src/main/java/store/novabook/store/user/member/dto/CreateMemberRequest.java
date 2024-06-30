package store.novabook.store.user.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateMemberRequest(

	@NotBlank(message = "아이디는 필수 입력 값입니다.")
	@Size(max = 20, message = "최대 20자까지 가능합니다.")
	String loginId,

	@NotBlank(message = "비밀번호는 필수 입력 값입니다.")
	@Size(max = 150, message = "최대 150자까지 가능합니다.")
	String loginPassword,

	@NotBlank(message = "비밀번호는 필수 입력 값입니다.")
	@Size(max = 150, message = "최대 150자까지 가능합니다.")
	String loginPasswordConfirm,

	@NotBlank(message = "ㅓ이름은 필수 입력 값입니다.")
	@Size(max = 50, message = "최대 50자까지 가능합니다.")
	String name,

	@NotBlank(message = "연락처는 필수 입력 값입니다.")
	@Size(max = 20, message = "최대 20자까지 가능합니다.")
	String number,

	@NotBlank(message = "이메일은 필수 입력 값입니다.")
	@Size(max = 100, message = "최대 100자까지 가능합니다.")
	String email,

	@NotBlank(message = "이메일은 필수 입력 값입니다.")
	String emailDomain,

	@NotNull(message = "생년월일은 필수 입력 값입니다.")
	Integer birthYear,

	@NotNull(message = "생년월일은 필수 입력 값입니다.")
	Integer birthMonth,

	@NotNull(message = "생년월일은 필수 입력 값입니다.")
	Integer birthDay,

	@NotBlank(message = "주소는 필수 입력 값입니다.")
	String address) {

	public String getEmailFull() {
		return email + "@" + emailDomain;
	}
}
