package store.novabook.store.book.controller.docs;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import store.novabook.store.book.dto.response.CreateLikesResponse;
import store.novabook.store.book.dto.response.GetLikeBookResponse;
import store.novabook.store.common.security.aop.CurrentUser;

@Tag(name = "Likes API")
public interface LikesControllerDocs {

	//member id 가 좋아요 누른 책들
	@Operation(summary = "좋아요 페이지 조회", description = "좋아요를 누른 모든 책들을 받습니다.")
	ResponseEntity<Page<GetLikeBookResponse>> getLikes(@CurrentUser Long memberId, Pageable pageable);

	//좋아요 누르면 하기 ( 프론트에서 좋아요를 눌렀었는지 아닌지 판단
	@Operation(summary = "좋아요 생성", description = "해당 도서에 좋아요 생성합니다")
	ResponseEntity<CreateLikesResponse> createLikes(@CurrentUser Long memberId, @RequestParam Long bookId);

	//좋아요 삭제
	@Operation(summary = "좋아요 삭제", description = "좋아요를 삭제 합니다.")
	ResponseEntity<Void> deleteLikes(@PathVariable Long likesId);
}
