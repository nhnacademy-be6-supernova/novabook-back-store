package store.novabook.store.point.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import store.novabook.store.point.entity.PointPolicy;
import store.novabook.store.point.service.PointService;

@RestController
@RequiredArgsConstructor
public class PointController {

	private final PointService pointService;

	@GetMapping("/points")
	public ResponseEntity<List<PointPolicy>> point(@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<PointPolicy> pointPolicyPage = pointService.getPointPolicyList(pageable);
		return new ResponseEntity<>(pointPolicyPage.getContent(), HttpStatus.OK);
	}

	@GetMapping("/points/latest")
	public ResponseEntity<PointPolicy> latestPoint() {
		PointPolicy pointPolicy = pointService.getLatestPointPolicy();
		return new ResponseEntity<>(pointPolicy, HttpStatus.OK);
	}
}
