package store.novabook.store.point.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import store.novabook.store.point.dto.request.CreatePointHistoryRequest;
import store.novabook.store.point.dto.request.GetPointHistoryRequest;
import store.novabook.store.point.dto.response.GetPointHistoryListResponse;
import store.novabook.store.point.dto.response.GetPointHistoryResponse;

public interface PointHistoryService {
	@Transactional(readOnly = true)
	Page<GetPointHistoryResponse> getPointHistoryList(Pageable pageable);

	@Transactional(readOnly = true)
	GetPointHistoryListResponse getPointHistory(GetPointHistoryRequest getPointHistoryRequest);

	void createPointHistory(CreatePointHistoryRequest createPointHistoryRequest);
}
