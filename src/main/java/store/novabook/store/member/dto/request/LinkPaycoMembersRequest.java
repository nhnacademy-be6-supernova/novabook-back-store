package store.novabook.store.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LinkPaycoMembersRequest(
	@NotNull
	Long membersId,
	@NotBlank
	String oauthId
) {
}
