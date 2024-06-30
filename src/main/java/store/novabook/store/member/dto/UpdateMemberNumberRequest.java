package store.novabook.store.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateMemberNumberRequest(
	@NotBlank(message = "연락처는 필수 입력 값입니다.")
	@Size(max = 20, message = "최대 20자까지 가능합니다.")
	String number) {
}
