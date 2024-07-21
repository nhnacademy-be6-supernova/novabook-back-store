package store.novabook.store.member.controller.docs;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import store.novabook.store.common.adatper.dto.GetCouponAllResponse;
import store.novabook.store.common.adatper.dto.GetCouponHistoryResponse;
import store.novabook.store.common.adatper.dto.GetUsedCouponHistoryResponse;
import store.novabook.store.member.dto.request.CreateMemberCouponRequest;
import store.novabook.store.member.dto.request.DownloadCouponMessageRequest;
import store.novabook.store.member.dto.request.DownloadCouponRequest;
import store.novabook.store.member.dto.request.RegisterCouponRequest;
import store.novabook.store.member.dto.response.CreateMemberCouponResponse;
import store.novabook.store.member.dto.response.GetCouponIdsResponse;

/**
 * 회원 쿠폰 관련 API 요청을 처리하는 컨트롤러 문서화 인터페이스.
 */
@Tag(name = "MemberCoupon API", description = "회원의 쿠폰 API")
public interface MemberCouponControllerDocs {
	/**
	 * 회원 쿠폰 생성
	 *
	 * @param memberId 회원 ID
	 * @param request 생성할 쿠폰 정보
	 * @return 생성된 쿠폰 정보
	 */
	@Operation(summary = "회원 쿠폰 생성", description = "회원에게 새 쿠폰을 생성합니다.")
	@Parameter(description = "회원 ID")
	@Parameter(description = "생성할 쿠폰 정보")
	ResponseEntity<CreateMemberCouponResponse> createMemberCoupon(Long memberId, CreateMemberCouponRequest request);

	/**
	 * 회원 쿠폰 등록
	 *
	 * @param memberId 회원 ID
	 * @param request 등록할 쿠폰 정보
	 * @return 등록된 쿠폰 정보
	 */
	@Operation(summary = "회원 쿠폰 등록", description = "회원에게 쿠폰을 등록합니다.")
	@Parameter(description = "회원 ID")
	@Parameter(description = "등록할 쿠폰 정보")
	ResponseEntity<CreateMemberCouponResponse> registerMemberCoupon(Long memberId, RegisterCouponRequest request);

	/**
	 * 회원의 쿠폰 사용 내역 조회
	 *
	 * @param memberId 회원 ID
	 * @param pageable 페이징 정보
	 * @return 회원의 쿠폰 사용 내역
	 */
	@Operation(summary = "회원 쿠폰 사용 내역 조회", description = "회원의 쿠폰 사용 내역을 조회합니다.")
	@Parameter(description = "회원 ID")
	@Parameter(description = "페이징 정보")
	ResponseEntity<Page<GetCouponHistoryResponse>> getMemberCouponHistoryByMemberId(Long memberId, Pageable pageable);

	/**
	 * 회원의 사용한 쿠폰 내역 조회
	 *
	 * @param memberId 회원 ID
	 * @param pageable 페이징 정보
	 * @return 회원의 사용한 쿠폰 내역
	 */
	@Operation(summary = "회원 사용 쿠폰 내역 조회", description = "회원이 사용한 쿠폰 내역을 조회합니다.")
	ResponseEntity<Page<GetUsedCouponHistoryResponse>> getMemberUsedCouponHistoryByMemberId(
		@Parameter(description = "회원 ID") Long memberId, @Parameter(description = "페이징 정보") Pageable pageable);

	/**
	 * 회원의 유효한 모든 쿠폰 조회
	 *
	 * @param memberId 회원 ID
	 * @return 유효한 모든 쿠폰 정보
	 */
	@Operation(summary = "회원 유효한 쿠폰 조회", description = "회원의 유효한 모든 쿠폰을 조회합니다.")
	@Parameter(description = "회원 ID")
	ResponseEntity<GetCouponAllResponse> getMemberCouponByMemberId(Long memberId);

	/**
	 * 회원의 쿠폰 ID 조회
	 *
	 * @param memberId 회원 ID
	 * @return 회원의 쿠폰 ID
	 */
	@Operation(summary = "회원 쿠폰 ID 조회", description = "회원의 쿠폰 ID를 조회합니다.")
	@Parameter(description = "회원 ID")
	ResponseEntity<GetCouponIdsResponse> getMemberCoupon(Long memberId);

	/**
	 * 쿠폰북 페이지 다운로드
	 *
	 * @param memberId 회원 ID
	 * @param request 다운로드할 쿠폰 정보
	 * @return 다운로드된 쿠폰 정보
	 */
	@Operation(summary = "쿠폰북 페이지 다운로드", description = "쿠폰북 페이지를 다운로드합니다.")
	@Parameter(description = "회원 ID")
	@Parameter(description = "다운로드할 쿠폰 정보")
	ResponseEntity<CreateMemberCouponResponse> downloadCoupon(Long memberId, DownloadCouponRequest request);

	/**
	 * 선착순 쿠폰 다운로드
	 *
	 * @param token 인증 토큰
	 * @param refresh 리프레시 토큰
	 * @param memberId 회원 ID
	 * @param request 다운로드할 쿠폰 정보
	 * @return 다운로드 결과
	 */
	@Operation(summary = "선착순 쿠폰 다운로드", description = "선착순 쿠폰을 다운로드합니다.")
	@Parameter(description = "인증 토큰")
	@Parameter(description = "리프레시 토큰")
	@Parameter(description = "회원 ID")
	@Parameter(description = "다운로드할 쿠폰 정보")
	ResponseEntity<Void> downloadLimitedCoupon(String token, String refresh, Long memberId,
		DownloadCouponMessageRequest request);
}
