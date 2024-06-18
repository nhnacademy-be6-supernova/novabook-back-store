package store.novabook.store.point.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import store.novabook.handler.exception.EntityNotFoundException;
import store.novabook.store.point.dto.CreatePointHistoryRequest;
import store.novabook.store.point.dto.GetPointHistoryResponse;
import store.novabook.store.point.entity.PointHistory;
import store.novabook.store.point.repository.PointHistoryRepository;

@Service
@RequiredArgsConstructor
public class PointHistoryService {

	private final PointHistoryRepository pointHistoryRepository;

	public Page<GetPointHistoryResponse> getPointHistoryList(Pageable pageable) {
		Page<PointHistory> pointHistoryList = pointHistoryRepository.findAll(pageable);
		if (pointHistoryList.isEmpty()) {
			throw new EntityNotFoundException(PointHistory.class);
		}
		return pointHistoryList.map(pointHistory -> new GetPointHistoryResponse(
			pointHistory.getOrders(),
			pointHistory.getPointPolicy(),
			pointHistory.getMember(),
			pointHistory.getPointContent(),
			pointHistory.getPointAmount()
		));

	}

	public void savePointHistory(CreatePointHistoryRequest createPointHistoryRequest) {
		PointHistory pointHistory = new PointHistory(null,
			createPointHistoryRequest.orders(),
			createPointHistoryRequest.pointPolicy(),
			createPointHistoryRequest.member(),
			createPointHistoryRequest.pointContent(),
			createPointHistoryRequest.pointAmount(),
			LocalDateTime.now(),
			null);

		pointHistoryRepository.save(pointHistory);
	}
}
