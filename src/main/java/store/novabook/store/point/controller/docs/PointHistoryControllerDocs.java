package store.novabook.store.point.controller.docs;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import store.novabook.store.common.security.aop.CurrentMembers;
import store.novabook.store.point.dto.response.GetPointHistoryResponse;
import store.novabook.store.point.dto.response.GetPointResponse;

/**
 * 포인트 내역 관련 API 요청을 처리하는 컨트롤러.
 */
@Tag(name = "PointHistory API", description = "포인트 내역 API")
public interface PointHistoryControllerDocs {

	/**
	 * 모든 포인트 내역을 페이지네이션으로 반환합니다.
	 *
	 * @param pageable 페이지 정보
	 * @return 포인트 내역 페이지
	 */
	@Operation(summary = "포인트 내역 조회", description = "포인트 내역을 조회합니다.")
	ResponseEntity<Page<GetPointHistoryResponse>> getPointHistoryList(Pageable pageable);

	/**
	 * 특정 회원의 포인트 총합을 조회합니다.
	 *
	 * @param memberId 현재 사용자 ID
	 * @return 회원의 포인트 총합
	 */
	@Operation(summary = "회원 포인트 총합 조회", description = "특정 회원의 포인트 총합을 조회합니다.")
	ResponseEntity<GetPointResponse> getPointTotalByMemberId(@CurrentMembers Long memberId);

	/**
	 * 특정 회원의 포인트 내역을 페이지네이션으로 반환합니다.
	 *
	 * @param memberId 현재 사용자 ID
	 * @param pageable 페이지 정보
	 * @return 회원의 포인트 내역 페이지
	 */
	@Operation(summary = "회원 포인트 내역 조회", description = "특정 회원의 포인트 내역을 조회합니다.")
	ResponseEntity<Page<GetPointHistoryResponse>> getPointHistoryByMemberIdPage(@CurrentMembers Long memberId,
		Pageable pageable);
}
