package store.novabook.store.point.dto;

import lombok.Builder;

@Builder
public record PointPolicyResponse(
	long reviewPoint,
	long createAt,
	long updateAt) {
}
