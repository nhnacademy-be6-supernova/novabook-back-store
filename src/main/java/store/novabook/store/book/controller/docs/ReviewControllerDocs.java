package store.novabook.store.book.controller.docs;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import store.novabook.store.book.dto.request.CreateReviewRequest;
import store.novabook.store.book.dto.request.UpdateReviewRequest;
import store.novabook.store.book.dto.response.CreateReviewResponse;
import store.novabook.store.book.dto.response.GetOrdersBookReviewIdResponse;
import store.novabook.store.book.dto.response.GetReviewResponse;
import store.novabook.store.book.dto.response.SearchBookResponse;
import store.novabook.store.common.security.aop.CurrentUser;

/**
 * 리뷰 관련 API 요청을 처리하는 컨트롤러.
 */
@Tag(name = "Review API")
public interface ReviewControllerDocs {

	/**
	 * 특정 회원이 작성한 리뷰가 포함된 책들을 페이지네이션으로 반환합니다.
	 * @param pageable 페이지 정보
	 * @return 페이지화된 책 정보
	 */
	@Operation(summary = "도서 페이지 조회", description = "리뷰가 존재하는 책들을 조회합니다.")
	ResponseEntity<Page<SearchBookResponse>> getReviewedBooks(@CurrentUser Long memberId, Pageable pageable);

	/**
	 * 특정 회원이 작성한 모든 리뷰를 페이지네이션으로 반환합니다.
	 * @param pageable 페이지 정보
	 * @return 페이지화된 리뷰 정보
	 */
	@Operation(summary = "회원 리뷰 조회", description = "회원이 작성한 리뷰들을 조회합니다.")
	ResponseEntity<Page<GetReviewResponse>> getReviewByMember(@CurrentUser Long memberId, Pageable pageable);


		/**
		 * 특정 책에 대한 모든 리뷰를 페이지네이션으로 반환합니다.
		 * @param bookId 책 ID
		 * @param pageable 페이지 정보
		 * @return 페이지화된 리뷰 정보
		 */
	@Operation(summary = "도서 리뷰 조회", description = "도서에 작성된 리뷰들을 조회합니다.")
	ResponseEntity<Page<GetReviewResponse>> getReviewByBookId(@PathVariable Long bookId, Pageable pageable);

	/**
	 * 새로운 리뷰를 작성합니다.
	 * @param request 리뷰 생성 요청 데이터
	 * @return 생성된 리뷰의 응답 데이터
	 */
	@Operation(summary = "리뷰 작성", description = "리뷰를 작성합니다.")
	ResponseEntity<CreateReviewResponse> createReviewed(@CurrentUser Long memberId, @Valid @RequestBody CreateReviewRequest request);

	/**
	 * 특정 리뷰를 업데이트합니다.
	 * @param request 리뷰 업데이트 요청 데이터
	 * @param reviewsId 수정할 리뷰의 ID
	 * @return 응답 상태 코드
	 */
	@Operation(summary = "리뷰 수정", description = "리뷰를 수정합니다.")
	ResponseEntity<Void> updateReviewed(@CurrentUser Long memberId, @Valid @RequestBody UpdateReviewRequest request, @PathVariable Long reviewsId);
}
