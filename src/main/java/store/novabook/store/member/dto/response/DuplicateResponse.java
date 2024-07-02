package store.novabook.store.member.dto.response;

import lombok.Builder;

@Builder
public record DuplicateResponse(
	Boolean isDuplicate) {
}
