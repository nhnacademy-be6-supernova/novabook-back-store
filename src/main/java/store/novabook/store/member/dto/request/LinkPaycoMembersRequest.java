package store.novabook.store.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LinkPaycoMembersRequest(
	@NotBlank
	Long membersId,
	@NotBlank
	String oauthId
) {
}
