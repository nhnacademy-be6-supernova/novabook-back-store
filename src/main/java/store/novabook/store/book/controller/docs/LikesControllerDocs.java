package store.novabook.store.book.controller.docs;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import store.novabook.store.book.dto.response.GetLikeBookResponse;
import store.novabook.store.book.dto.response.LikeBookResponse;
import store.novabook.store.common.security.aop.CurrentUser;

/**
 * LikesControllerDocs 인터페이스는 좋아요 기능에 대한 API 문서를 제공합니다.
 */
@Tag(name = "Likes API")
public interface LikesControllerDocs {

	/**
	 * 좋아요 페이지 조회
	 *
	 * @param memberId 좋아요를 누른 사용자의 ID
	 * @param pageable 페이지 요청 정보
	 * @return 좋아요를 누른 책들의 페이지
	 */
	@Operation(summary = "좋아요 페이지 조회", description = "좋아요를 누른 모든 책들을 받습니다.")
	ResponseEntity<Page<GetLikeBookResponse>> getLikes(@CurrentUser Long memberId, Pageable pageable);

	/**
	 * 좋아요 생성
	 *
	 * @param memberId 좋아요를 누른 사용자의 ID
	 * @param bookId 좋아요를 누를 책의 ID
	 * @return 생성된 좋아요 정보
	 */
	@Operation(summary = "좋아요 생성", description = "해당 도서에 좋아요 생성합니다")
	ResponseEntity<LikeBookResponse> likeButton(@CurrentUser Long memberId, @PathVariable Long bookId);

	/**
	 * 좋아요 삭제
	 *
	 * @param bookId 책 ID로 좋아요 상태 확인
	 * @param memberId 사용자 ID로 좋아요 상태 확인
	 * @return 좋아요 상태 유무
	 */
	@Operation(summary = "좋아요 상태 유무", description = "좋아요 상태 유무를 확인합니다.")
	ResponseEntity<LikeBookResponse> isLiked(@PathVariable Long bookId, @CurrentUser Long memberId);
}
