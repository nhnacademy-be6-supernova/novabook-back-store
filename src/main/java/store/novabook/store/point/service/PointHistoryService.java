package store.novabook.store.point.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import store.novabook.store.point.dto.request.CreatePointHistoryRequest;
import store.novabook.store.point.dto.response.GetPointHistoryResponse;
import store.novabook.store.point.dto.response.GetPointResponse;

public interface PointHistoryService {
	Page<GetPointHistoryResponse> getPointHistoryList(Pageable pageable);

	Page<GetPointHistoryResponse> getPointHistoryByMemberIdPage(Long memberId, Pageable pageable);

	void createPointHistory(CreatePointHistoryRequest createPointHistoryRequest);

	GetPointResponse getPointTotalByMemberId(Long memberId);
}
