package store.novabook.store.book.controller.docs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import store.novabook.store.book.dto.request.CreateReviewRequest;
import store.novabook.store.book.dto.request.UpdateReviewRequest;
import store.novabook.store.book.dto.response.CreateReviewResponse;
import store.novabook.store.book.dto.response.GetReviewListResponse;
import store.novabook.store.book.dto.response.GetReviewResponse;
import store.novabook.store.common.security.aop.CurrentUser;

/**
 * 리뷰 관련 API 요청을 처리하는 컨트롤러.
 */
@Tag(name = "리뷰 API")
public interface ReviewControllerDocs {

	/**
	 * 특정 리뷰를 조회합니다.
	 *
	 * @param reviewId 조회할 리뷰의 ID
	 * @return ResponseEntity<GetReviewResponse> 조회된 리뷰 정보와 HTTP 상태 코드를 포함한 응답
	 */
	ResponseEntity<GetReviewResponse> getReview(@PathVariable Long reviewId);

	/**
	 * 특정 책에 대한 모든 리뷰를 페이지네이션으로 반환합니다.
	 * @param bookId 책 ID
	 * @return 리뷰 정보
	 */
	@Operation(summary = "도서 리뷰 조회", description = "도서에 작성된 리뷰들을 조회합니다.")
	ResponseEntity<GetReviewListResponse> getReviewByBookId(@PathVariable Long bookId);

	/**
	 * 새로운 리뷰를 작성합니다.
	 * @param ordersBookId 주문 도서 ID
	 * @param request 리뷰 생성 요청 데이터
	 * @param memberId 현재 사용자 ID
	 * @return 생성된 리뷰의 응답 데이터
	 */
	@Operation(summary = "리뷰 작성", description = "리뷰를 작성합니다.")
	ResponseEntity<CreateReviewResponse> createReviewed(
		@Parameter(description = "주문 도서 ID", required = true) @Valid @PathVariable Long ordersBookId,
		@Parameter(description = "리뷰 생성 요청 데이터", required = true) @Valid @RequestBody CreateReviewRequest request,
		@Parameter(description = "현재 사용자 ID", required = true) @CurrentUser Long memberId);

	/**
	 * 특정 리뷰를 업데이트합니다.
	 * @param request 리뷰 업데이트 요청 데이터
	 * @param reviewsId 수정할 리뷰의 ID
	 * @return 응답 상태 코드
	 */
	@Operation(summary = "리뷰 수정", description = "리뷰를 수정합니다.")
	ResponseEntity<Void> updateReviewed(
		@Parameter(description = "리뷰 업데이트 요청 데이터", required = true) @Valid @RequestBody UpdateReviewRequest request,
		@Parameter(description = "수정할 리뷰의 ID", required = true) @PathVariable Long reviewsId);
}
