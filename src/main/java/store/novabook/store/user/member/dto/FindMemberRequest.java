package store.novabook.store.user.member.dto;

import jakarta.validation.constraints.NotBlank;

public record FindMemberRequest(
	@NotBlank
	String loginId
) {
}
