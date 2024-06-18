package store.novabook.store.point.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.handler.exception.EntityNotFoundException;
import store.novabook.handler.exception.ErrorStatus;
import store.novabook.store.point.dto.CreatePointHistoryRequest;
import store.novabook.store.point.dto.CreatePointPolicyRequest;
import store.novabook.store.point.entity.PointHistory;
import store.novabook.store.point.entity.PointPolicy;
import store.novabook.store.point.repository.PointHistoryRepository;
import store.novabook.store.point.repository.PointPolicyRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class PointService {

	private final PointPolicyRepository pointPolicyRepository;
	private final PointHistoryRepository pointHistoryRepository;

	public Page<PointPolicy> getPointPolicyList(Pageable pageable) {
		Page<PointPolicy> pointPolicyList = pointPolicyRepository.findAll(pageable);
		if (pointPolicyList.isEmpty()) {
			// throw new PointPolicyEmptyException();
		}
		return pointPolicyList;
	}

	public PointPolicy getLatestPointPolicy() {
		return pointPolicyRepository.findTopByOrderByCreatedAtDesc()
			.orElseThrow(
				() -> new EntityNotFoundException(new ErrorStatus("최신 포인트 정책이 존재하지 않습니다.", 404, LocalDateTime.now())));
	}

	public PointPolicy savePointPolicy(CreatePointPolicyRequest createPointPolicyRequest) {
		PointPolicy pointPolicy = new PointPolicy(null, createPointPolicyRequest.reviewPointRate(),
			createPointPolicyRequest.basicPoint(), createPointPolicyRequest.registerPoint(), LocalDateTime.now(), null);
		return pointPolicyRepository.save(pointPolicy);
	}

	public Page<PointHistory> getPointHistoryList(Pageable pageable) {
		return pointHistoryRepository.findAll(pageable);
	}

	public PointHistory savePointHistory(CreatePointHistoryRequest createPointHistoryRequest) {
		PointHistory pointHistory = new PointHistory(null,
			createPointHistoryRequest.orders(),
			createPointHistoryRequest.pointPolicy(),
			createPointHistoryRequest.member(),
			createPointHistoryRequest.pointContent(),
			createPointHistoryRequest.pointAmount(),
			LocalDateTime.now(),
			null);

		return pointHistoryRepository.save(pointHistory);
	}
}