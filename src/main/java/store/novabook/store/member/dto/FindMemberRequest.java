package store.novabook.store.member.dto;

import jakarta.validation.constraints.NotBlank;

public record FindMemberRequest(
	@NotBlank
	String loginId
) {
}
