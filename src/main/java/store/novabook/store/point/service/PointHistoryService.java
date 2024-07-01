package store.novabook.store.point.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import store.novabook.store.point.dto.request.CreatePointHistoryRequest;
import store.novabook.store.point.dto.response.GetPointHistoryResponse;

public interface PointHistoryService {
	@Transactional(readOnly = true)
	Page<GetPointHistoryResponse> getPointHistoryList(Pageable pageable);

	void createPointHistory(CreatePointHistoryRequest createPointHistoryRequest);
}
