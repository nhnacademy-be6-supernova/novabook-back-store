package store.novabook.store.point.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.store.common.security.aop.CurrentUser;
import store.novabook.store.point.controller.docs.PointPolicyControllerDocs;
import store.novabook.store.point.dto.request.CreatePointPolicyRequest;
import store.novabook.store.point.dto.response.GetPointPolicyResponse;
import store.novabook.store.point.service.PointPolicyService;

@RestController
@RequestMapping("/api/v1/store/point/policies")
@RequiredArgsConstructor
public class PointPolicyController implements PointPolicyControllerDocs {

	private final PointPolicyService pointPolicyService;

	// @CheckRole("ROLE_USER")
	@GetMapping
	public ResponseEntity<Page<GetPointPolicyResponse>> getPoint(@CurrentUser String id, Pageable pageable) {
		Page<GetPointPolicyResponse> pointPolicyResponseList = pointPolicyService.getPointPolicyList(pageable);

		return ResponseEntity.status(HttpStatus.OK).body(pointPolicyResponseList);
	}

	@GetMapping("/latest")
	public ResponseEntity<GetPointPolicyResponse> getLatestPoint() {
		GetPointPolicyResponse getPointPolicyResponse = pointPolicyService.getLatestPointPolicy();
		return ResponseEntity.status(HttpStatus.OK).body(getPointPolicyResponse);
	}

	@PostMapping
	public ResponseEntity<Void> createPointPolicy(
		@Valid @RequestBody CreatePointPolicyRequest createPointPolicyRequest) {
		pointPolicyService.createPointPolicy(createPointPolicyRequest);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

}
