package store.novabook.store.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record GetDormantMembersRequest(
	@NotBlank
	Long membersId) {
}
