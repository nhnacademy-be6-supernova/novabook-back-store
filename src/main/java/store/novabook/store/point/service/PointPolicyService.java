package store.novabook.store.point.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import store.novabook.store.point.dto.CreatePointPolicyRequest;
import store.novabook.store.point.dto.GetPointPolicyResponse;

public interface PointPolicyService {
	@Transactional(readOnly = true)
	Page<GetPointPolicyResponse> getPointPolicyList(Pageable pageable);

	@Transactional(readOnly = true)
	GetPointPolicyResponse getLatestPointPolicy();

	void createPointPolicy(CreatePointPolicyRequest createPointPolicyRequest);
}
