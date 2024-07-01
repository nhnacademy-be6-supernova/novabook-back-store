package store.novabook.store.point.service.impl;

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
import store.novabook.store.point.service.PointPolicyService;

@Service
@RequiredArgsConstructor
@Transactional
public class PointPolicyServiceImpl implements PointPolicyService {

	private final PointPolicyRepository pointPolicyRepository;

	@Override
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

	@Override
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

	@Override
	public void createPointPolicy(CreatePointPolicyRequest createPointPolicyRequest) {
		PointPolicy pointPolicy = PointPolicy.of(
			createPointPolicyRequest.reviewPointRate(),
			createPointPolicyRequest.basicPoint(),
			createPointPolicyRequest.registerPoint());
		pointPolicyRepository.save(pointPolicy);
	}

}