package store.novabook.store.common.security.dto;

import jakarta.validation.constraints.NotBlank;

public record FindMembersRequest(
	@NotBlank
	String memberId
) {
}
