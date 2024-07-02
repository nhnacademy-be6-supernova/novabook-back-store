package store.novabook.store.member.dto.request;

import jakarta.validation.constraints.Size;

public record UpdateMemberRequest(
	@Size(max = 50, message = "최대 50자까지 가능합니다.")
	String name,

	@Size(max = 20, message = "최대 20자까지 가능합니다.")
	String number) {
}
