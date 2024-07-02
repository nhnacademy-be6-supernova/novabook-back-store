package store.novabook.store.point.controller.docs;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import store.novabook.store.common.security.aop.CurrentUser;
import store.novabook.store.point.dto.request.CreatePointPolicyRequest;
import store.novabook.store.point.dto.response.GetPointPolicyResponse;

@Tag(name = "PointPolicy API", description = "포인트 정책 API")
public interface PointPolicyControllerDocs {
	@Operation(summary = "포인트 정책 조회", description = "포인트 정책을 조회합니다.")
	ResponseEntity<Page<GetPointPolicyResponse>> getPoint(@CurrentUser String id, Pageable pageable);

	@Operation(summary = "최신 포인트 정책 조회", description = "최신 포인트 정책을 조회합니다 포인트 정책은 최신 포인트 정책으로 적용")
	@Parameter(name = "getPointPolicyResponse", description = "최신 포인트 정책 조회 정보", required = true)
	ResponseEntity<GetPointPolicyResponse> getLatestPoint();

	@Operation(summary = "포인트 정책 생성", description = "포인트 정책을 생성합니다.")
	@Parameter(name = "createPointPolicyRequest", description = "포인트 정책 생성 정보", required = true)
	ResponseEntity<Void> createPointPolicy(
		@Valid @RequestBody CreatePointPolicyRequest createPointPolicyRequest);

}
