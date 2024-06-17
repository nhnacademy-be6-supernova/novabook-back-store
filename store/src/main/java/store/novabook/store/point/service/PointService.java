package store.novabook.store.point.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.point.dto.PointHistoryCreateRequest;
import store.novabook.store.point.dto.PointPolicyCreateRequest;
import store.novabook.store.point.entity.PointHistory;
import store.novabook.store.point.entity.PointPolicy;
import store.novabook.store.point.exception.PointPolicyEmptyException;
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
			throw new PointPolicyEmptyException();
		}
		return pointPolicyList;
	}

	public PointPolicy getLatestPointPolicy() {
		// return pointPolicyRepository.findTopByOrderByCreatedDateDesc().orElseThrow(PointPolicyEmptyException::new);
		return null;
	}

	public PointPolicy savePointPolicy(PointPolicyCreateRequest pointPolicyCreateRequest) {
		PointPolicy pointPolicy = new PointPolicy(null, pointPolicyCreateRequest.reviewPointRate(),
			pointPolicyCreateRequest.basicPoint(), pointPolicyCreateRequest.registerPoint(), LocalDateTime.now(), null);
		return pointPolicyRepository.save(pointPolicy);
	}

	public Page<PointHistory> getPointHistoryList(Pageable pageable) {
		return pointHistoryRepository.findAll(pageable);
	}

	public PointHistory savePointHistory(PointHistoryCreateRequest pointHistoryCreateRequest) {
		PointHistory pointHistory = new PointHistory(null, pointHistoryCreateRequest.orders(),
			pointHistoryCreateRequest.pointPolicy(), pointHistoryCreateRequest.pointContent(),
			pointHistoryCreateRequest.pointAmount(), LocalDateTime.now(), null);

		return pointHistoryRepository.save(pointHistory);
	}
}