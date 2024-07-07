package store.novabook.store.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record GetPaycoMembersRequest(
	@NotBlank
	String paycoId
) {
}
