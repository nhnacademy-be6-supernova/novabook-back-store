package store.novabook.store.point.dto.response;

import lombok.Builder;

@Builder
public record GetPointResponse(
	Long pointAmount) {
}
