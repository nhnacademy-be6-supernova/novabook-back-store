package store.novabook.store.member.dto.request;

import jakarta.validation.constraints.NotNull;

public record GetDormantMembersRequest(
	@NotNull
	Long membersId) {
}
