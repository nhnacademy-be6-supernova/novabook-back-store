package store.novabook.store.orders.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.store.orders.dto.CreateResponse;
import store.novabook.store.orders.dto.CreateReturnPolicyRequest;
import store.novabook.store.orders.dto.GetReturnPolicyResponse;
import store.novabook.store.orders.service.ReturnPolicyService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/return/policy")
public class ReturnPolicyController {
	private final ReturnPolicyService returnPolicyService;

	//생성
	@PostMapping
	public ResponseEntity<CreateResponse> createReturnPolicy(@Valid @RequestBody CreateReturnPolicyRequest request) {
		CreateResponse response = returnPolicyService.save(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	//전체 조회
	@GetMapping
	public ResponseEntity<Page<GetReturnPolicyResponse>> getReturnPolicyAll(){
		Page<GetReturnPolicyResponse> response = returnPolicyService.getReturnPolicies();
		return ResponseEntity.ok(response);
	}

	//단건 조회
	@GetMapping("{id}")
	public ResponseEntity<GetReturnPolicyResponse> getReturnPolicy(@PathVariable Long id) {
		GetReturnPolicyResponse response = returnPolicyService.getReturnPolicyById(id);
		return ResponseEntity.ok(response);
	}
}
