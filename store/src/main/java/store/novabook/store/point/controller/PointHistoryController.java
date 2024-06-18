package store.novabook.store.point.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import store.novabook.store.point.dto.CreatePointHistoryRequest;
import store.novabook.store.point.dto.CreatePointPolicyRequest;
import store.novabook.store.point.entity.PointHistory;
import store.novabook.store.point.entity.PointPolicy;
import store.novabook.store.point.service.PointService;

@RestController
@RequiredArgsConstructor
public class PointHistoryController {

	private final PointService pointService;

	@GetMapping("/point/policies")
	public ResponseEntity<List<PointPolicy>> point(Pageable pageable) {
		Page<PointPolicy> pointPolicyPage = pointService.getPointPolicyList(pageable);
		return new ResponseEntity<>(pointPolicyPage.getContent(), HttpStatus.OK);
	}

	@GetMapping("/point/policy/latest")
	public ResponseEntity<PointPolicy> latestPoint() {
		PointPolicy pointPolicy = pointService.getLatestPointPolicy();
		return new ResponseEntity<>(pointPolicy, HttpStatus.OK);
	}

	@PostMapping("/point/policy")
	public ResponseEntity<PointPolicy> savePointPolicy(
		@ModelAttribute CreatePointPolicyRequest createPointPolicyRequest) {

		PointPolicy pointPolicy = pointService.savePointPolicy(createPointPolicyRequest);
		return new ResponseEntity<>(pointPolicy, HttpStatus.CREATED);
	}

	@GetMapping("/point/histories")
	public ResponseEntity<List<PointHistory>> pointHistories(Pageable pageable) {
		Page<PointHistory> pointHistoryPage = pointService.getPointHistoryList(pageable);
		return new ResponseEntity<>(pointHistoryPage.getContent(), HttpStatus.OK);
	}

	@PostMapping("/point/history")
	public ResponseEntity<PointHistory> savePointHistory(
		@ModelAttribute CreatePointHistoryRequest createPointHistoryRequest) {
		PointHistory pointHistory = pointService.savePointHistory(createPointHistoryRequest);
		return new ResponseEntity<>(pointHistory, HttpStatus.CREATED);
	}

}
