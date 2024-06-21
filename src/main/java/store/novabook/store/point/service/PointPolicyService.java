package store.novabook.store.point.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.common.exception.EntityNotFoundException;
import store.novabook.store.point.dto.CreatePointPolicyRequest;
import store.novabook.store.point.dto.GetPointPolicyResponse;
import store.novabook.store.point.entity.PointPolicy;
import store.novabook.store.point.repository.PointPolicyRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class PointPolicyService {

	private final PointPolicyRepository pointPolicyRepository;

	@Transactional(readOnly = true)
	public Page<GetPointPolicyResponse> getPointPolicyList(Pageable pageable) {
		Page<PointPolicy> pointPolicyList = pointPolicyRepository.findAll(pageable);
		if (pointPolicyList.isEmpty()) {
			throw new EntityNotFoundException(PointPolicy.class);
		}
		return pointPolicyList.map(pointPolicy -> new GetPointPolicyResponse(
			pointPolicy.getReviewPointRate(),
			pointPolicy.getBasicPoint(),
			pointPolicy.getRegisterPoint()
		));
	}

	@Transactional(readOnly = true)
	public GetPointPolicyResponse getLatestPointPolicy() {
		PointPolicy pointPolicy = pointPolicyRepository.findTopByOrderByCreatedAtDesc()
			.orElseThrow(
				() -> new EntityNotFoundException(PointPolicy.class));
		return GetPointPolicyResponse.builder()
			.reviewPointRate(pointPolicy.getReviewPointRate())
			.basicPoint(pointPolicy.getBasicPoint())
			.registerPoint(pointPolicy.getRegisterPoint()).build();
	}

	public void createPointPolicy(CreatePointPolicyRequest createPointPolicyRequest) {
		PointPolicy pointPolicy = new PointPolicy(null,
			createPointPolicyRequest.reviewPointRate(),
			createPointPolicyRequest.basicPoint(),
			createPointPolicyRequest.registerPoint(),
			LocalDateTime.now(),
			null);
		pointPolicyRepository.save(pointPolicy);
	}

}