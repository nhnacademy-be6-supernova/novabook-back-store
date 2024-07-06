package store.novabook.store.point.dto.response;

import java.util.List;

import lombok.Builder;

@Builder
public record GetPointHistoryListResponse(
	List<GetPointHistoryResponse> pointHistoryResponseList
) {

}
