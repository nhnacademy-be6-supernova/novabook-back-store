package store.novabook.store.point.dto;

import lombok.Builder;

@Builder
public record GetPointPolicyResponse(
	long reviewPointRate,
	long basicPoint,
	long registerPoint) {
}
