package store.novabook.store.point.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import store.novabook.store.point.entity.PointHistory;

@Builder
public record GetPointHistoryResponse(
	String pointContent,
	long pointAmount,
	LocalDateTime createdAt
) {
	public static GetPointHistoryResponse of(PointHistory pointHistory) {
		return GetPointHistoryResponse.builder()
			.pointAmount(pointHistory.getPointAmount())
			.pointContent(pointHistory.getPointContent())
			.createdAt(pointHistory.getCreatedAt())
			.build();
	}
}
