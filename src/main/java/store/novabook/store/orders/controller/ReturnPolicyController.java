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
import store.novabook.store.common.security.aop.CheckRole;
import store.novabook.store.orders.controller.docs.ReturnPolicyControllerDocs;
import store.novabook.store.orders.dto.request.CreateReturnPolicyRequest;
import store.novabook.store.orders.dto.response.CreateResponse;
import store.novabook.store.orders.dto.response.GetReturnPolicyResponse;
import store.novabook.store.orders.service.ReturnPolicyService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/store/orders/return/policy")
public class ReturnPolicyController implements ReturnPolicyControllerDocs {

	private final ReturnPolicyService returnPolicyService;

	@CheckRole("ROLE_ADMIN")
	@PostMapping
	public ResponseEntity<CreateResponse> createReturnPolicy(@Valid @RequestBody CreateReturnPolicyRequest request) {
		CreateResponse response = returnPolicyService.save(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@CheckRole("ROLE_ADMIN")
	@GetMapping
	public ResponseEntity<Page<GetReturnPolicyResponse>> getReturnPolicyAll() {
		Page<GetReturnPolicyResponse> response = returnPolicyService.getReturnPolicies();
		return ResponseEntity.ok(response);
	}

	@CheckRole("ROLE_ADMIN")
	@GetMapping("{id}")
	public ResponseEntity<GetReturnPolicyResponse> getReturnPolicy(@PathVariable Long id) {
		GetReturnPolicyResponse response = returnPolicyService.getReturnPolicyById(id);
		return ResponseEntity.ok(response);
	}
}
