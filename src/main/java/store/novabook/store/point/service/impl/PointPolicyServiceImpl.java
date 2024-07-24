package store.novabook.store.point.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.common.exception.NotFoundException;
import store.novabook.store.point.dto.request.CreatePointPolicyRequest;
import store.novabook.store.point.dto.response.GetPointPolicyResponse;
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
		Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
		pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
		Page<PointPolicy> pointPolicyList = pointPolicyRepository.findAll(pageable);
		if (pointPolicyList.isEmpty()) {
			throw new NotFoundException(ErrorCode.POINT_POLICY_NOT_FOUND);
		}
		return pointPolicyList.map(
			pointPolicy -> new GetPointPolicyResponse(pointPolicy.getReviewPoint(), pointPolicy.getBasicPointRate(),
				pointPolicy.getRegisterPoint()));
	}

	@Override
	@Transactional(readOnly = true)
	public GetPointPolicyResponse getLatestPointPolicy() {
		PointPolicy pointPolicy = pointPolicyRepository.findTopByOrderByCreatedAtDesc()
			.orElseThrow(() -> new NotFoundException(ErrorCode.POINT_POLICY_NOT_FOUND));
		return GetPointPolicyResponse.builder()
			.reviewPointRate(pointPolicy.getReviewPoint())
			.basicPoint(pointPolicy.getBasicPointRate())
			.registerPoint(pointPolicy.getRegisterPoint())
			.build();
	}

	@Override
	public void createPointPolicy(CreatePointPolicyRequest createPointPolicyRequest) {
		PointPolicy pointPolicy = PointPolicy.of(createPointPolicyRequest.reviewPointRate(),
			createPointPolicyRequest.basicPoint(), createPointPolicyRequest.registerPoint());
		pointPolicyRepository.save(pointPolicy);
	}

}