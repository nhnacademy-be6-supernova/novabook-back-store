package store.novabook.store.orders.service;

import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import store.novabook.store.orders.dto.request.CreateReturnPolicyRequest;
import store.novabook.store.orders.dto.response.CreateResponse;
import store.novabook.store.orders.dto.response.GetReturnPolicyResponse;

public interface ReturnPolicyService {
	CreateResponse save(CreateReturnPolicyRequest request);

	String latestReturnPolicyContent();

	@Transactional(readOnly = true)
	Page<GetReturnPolicyResponse> getReturnPolicies();

	@Transactional(readOnly = true)
	GetReturnPolicyResponse getReturnPolicyById(Long id);
}
