package store.novabook.store.point.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import store.novabook.store.point.dto.CreatePointPolicyRequest;
import store.novabook.store.point.dto.GetPointPolicyResponse;
import store.novabook.store.point.service.PointPolicyService;

@RestController()
@RequestMapping("/point")
@RequiredArgsConstructor
public class PointPolicyController {
	private final PointPolicyService pointPolicyService;

	@GetMapping("/policies")
	public ResponseEntity<Page<GetPointPolicyResponse>> getPoint(Pageable pageable) {

		Page<GetPointPolicyResponse> pointPolicyResponseList = pointPolicyService.getPointPolicyList(pageable);
		return ResponseEntity.status(HttpStatus.OK).body(pointPolicyResponseList);
	}

	@GetMapping("/policies/latest")
	public ResponseEntity<GetPointPolicyResponse> getLatestPoint() {

		GetPointPolicyResponse getPointPolicyResponse = pointPolicyService.getLatestPointPolicy();
		return ResponseEntity.status(HttpStatus.OK).body(getPointPolicyResponse);
	}

	@PostMapping("/policies")
	public ResponseEntity<Void> createPointPolicy(
		@ModelAttribute CreatePointPolicyRequest createPointPolicyRequest) {

		pointPolicyService.savePointPolicy(createPointPolicyRequest);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

}
