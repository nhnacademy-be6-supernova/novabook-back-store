package store.novabook.store.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record GetMembersUUIDRequest(
	@NotBlank
	String uuid
) {
}
