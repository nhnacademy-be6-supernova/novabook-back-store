package store.novabook.store.point.dto;

import lombok.Builder;

@Builder
public record GetPointHistoryResponse(
	String pointContent,
	long pointAmount) {
}
