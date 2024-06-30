package store.novabook.store.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateMemberNameRequest(
	@NotBlank(message = "이름은 필수 입력 값입니다.")
	@Size(max = 50, message = "최대 50자까지 가능합니다.")
	String name
) {
}
