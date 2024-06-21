package store.novabook.store.orders.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.common.exception.EntityNotFoundException;
import store.novabook.store.orders.dto.CreateResponse;
import store.novabook.store.orders.dto.CreateReturnPolicyRequest;
import store.novabook.store.orders.dto.GetReturnPolicyResponse;
import store.novabook.store.orders.entity.ReturnPolicy;
import store.novabook.store.orders.repository.ReturnPolicyRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class ReturnPolicyService {
	private final ReturnPolicyRepository returnPolicyRepository;

	//생성
	public CreateResponse save(CreateReturnPolicyRequest request) {
		ReturnPolicy returnPolicy = new ReturnPolicy(request);
		returnPolicyRepository.save(returnPolicy);
		return new CreateResponse(returnPolicy.getId());
	}

	// 가장 최근 content
	public String latestReturnPolicyContent(){
		return returnPolicyRepository.findContentByOrderByIdDesc();
	}

	//전체 조회
	@Transactional(readOnly = true)
	public Page<GetReturnPolicyResponse> getReturnPolicies() {
		List<ReturnPolicy> returnPolicies = returnPolicyRepository.findAll();
		List<GetReturnPolicyResponse> responses = new ArrayList<>();
		returnPolicies.forEach(returnPolicy -> responses.add(GetReturnPolicyResponse.from(returnPolicy)));
		return new PageImpl<>(responses);
	}

	//단건 조회
	@Transactional(readOnly = true)
	public GetReturnPolicyResponse getReturnPolicyById(Long id) {
		ReturnPolicy returnPolicy = returnPolicyRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(ReturnPolicy.class,id));
		return GetReturnPolicyResponse.from(returnPolicy);
	}

}
