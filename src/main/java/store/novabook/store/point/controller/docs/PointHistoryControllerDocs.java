package store.novabook.store.point.controller.docs;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import store.novabook.store.point.dto.request.CreatePointHistoryRequest;
import store.novabook.store.point.dto.response.GetPointHistoryResponse;
import store.novabook.store.point.entity.PointHistory;

@Tag(name = "PointHistory API", description = "포인트 내역 API")
public interface PointHistoryControllerDocs {
	@Operation(summary = "포인트 내역 조회", description = "포인트 내역을 조회합니다.")
	ResponseEntity<Page<GetPointHistoryResponse>> getPointHistoryList(Pageable pageable);

	@Operation(summary = "포인트 내역 생성", description = "포인트 내역을 생성합니다.")
	@Parameter(name = "createPointHistoryRequest", description = "포인트 내역 생성 정보", required = true)
	ResponseEntity<PointHistory> createPointHistory(
		@Valid @RequestBody CreatePointHistoryRequest createPointHistoryRequest);
}
