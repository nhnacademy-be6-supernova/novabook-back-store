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
import store.novabook.store.point.controller.docs.PointHistoryControllerDocs;
import store.novabook.store.point.dto.request.CreatePointHistoryRequest;
import store.novabook.store.point.dto.request.GetPointHistoryRequest;
import store.novabook.store.point.dto.response.GetPointHistoryListResponse;
import store.novabook.store.point.dto.response.GetPointHistoryResponse;
import store.novabook.store.point.entity.PointHistory;
import store.novabook.store.point.service.PointHistoryService;

@RestController
@RequestMapping("/api/v1/store/point/histories")
@RequiredArgsConstructor
public class PointHistoryController implements PointHistoryControllerDocs {

	private final PointHistoryService pointHistoryService;

	@GetMapping(params = {"size", "page"})
	public ResponseEntity<Page<GetPointHistoryResponse>> getPointHistoryList(Pageable pageable) {
		Page<GetPointHistoryResponse> pointHistoryList = pointHistoryService.getPointHistoryList(pageable);
		return ResponseEntity.status(HttpStatus.OK).body(pointHistoryList);
	}

	@GetMapping
	public ResponseEntity<GetPointHistoryListResponse> getPointHistoryListByMemberId(@RequestBody GetPointHistoryRequest getPointHistoryRequest) {
		return ResponseEntity.ok().body(pointHistoryService.getPointHistory(getPointHistoryRequest));
	}

	@PostMapping
	public ResponseEntity<PointHistory> createPointHistory(
		@Valid @RequestBody CreatePointHistoryRequest createPointHistoryRequest) {
		pointHistoryService.createPointHistory(createPointHistoryRequest);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
