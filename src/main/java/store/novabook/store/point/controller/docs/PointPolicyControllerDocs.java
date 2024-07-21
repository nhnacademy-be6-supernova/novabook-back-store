package store.novabook.store.point.controller.docs;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import store.novabook.store.common.security.aop.CurrentMembers;
import store.novabook.store.point.dto.request.CreatePointPolicyRequest;
import store.novabook.store.point.dto.response.GetPointPolicyResponse;

/**
 * 포인트 정책 API 문서화 인터페이스입니다.
 * 이 인터페이스는 포인트 정책의 조회, 최신 정책 조회 및 정책 생성을 문서화합니다.
 */
@Tag(name = "PointPolicy API", description = "포인트 정책 API")
public interface PointPolicyControllerDocs {
	/**
	 * 페이지네이션을 이용하여 포인트 정책 목록을 조회합니다.
	 *
	 * @param id 현재 사용자 ID (어드민 권한 확인 용도)
	 * @param pageable 페이지 정보
	 * @return 포인트 정책 목록에 대한 응답
	 */
	@Operation(summary = "포인트 정책 조회", description = "페이지네이션을 이용하여 포인트 정책 목록을 조회합니다.")
	@Parameter(name = "id", description = "현재 사용자 ID (어드민 권한 확인 용도)", required = true)
	@Parameter(name = "pageable", description = "페이지 정보", required = true)
	ResponseEntity<Page<GetPointPolicyResponse>> getPoint(@CurrentMembers Long id, Pageable pageable);

	/**
	 * 최신 포인트 정책을 조회합니다.
	 *
	 * @return 최신 포인트 정책에 대한 응답
	 */
	@Operation(summary = "최신 포인트 정책 조회", description = "최신 포인트 정책을 조회합니다.")
	@Parameter(name = "getPointPolicyResponse", description = "최신 포인트 정책 조회 정보", required = true)
	ResponseEntity<GetPointPolicyResponse> getLatestPoint();

	/**
	 * 새로운 포인트 정책을 생성합니다.
	 *
	 * @param createPointPolicyRequest 생성할 포인트 정책 정보
	 * @return 생성 결과
	 */
	@Operation(summary = "포인트 정책 생성", description = "새로운 포인트 정책을 생성합니다.")
	@Parameter(name = "createPointPolicyRequest", description = "생성할 포인트 정책 정보", required = true)
	ResponseEntity<Void> createPointPolicy(@Valid @RequestBody CreatePointPolicyRequest createPointPolicyRequest);
}
