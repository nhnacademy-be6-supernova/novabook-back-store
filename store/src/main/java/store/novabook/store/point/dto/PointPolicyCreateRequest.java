package store.novabook.store.point.dto;

public record PointPolicyCreateRequest(Long reviewPointRate,
									   Long basicPoint,
									   Long registerPoint) {
}
