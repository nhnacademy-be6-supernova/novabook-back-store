package store.novabook.store.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record DoorayAuthCodeRequest(
	@NotBlank(message = "uuid 는 필수값입니다.")
	String uuid,

	@NotBlank(message = "인증코드는 필수값입니다.")
	String authCode) {
}
