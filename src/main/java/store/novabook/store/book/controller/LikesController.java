package store.novabook.store.book.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import store.novabook.store.book.controller.docs.LikesControllerDocs;
import store.novabook.store.book.dto.response.GetLikeBookResponse;
import store.novabook.store.book.dto.response.LikeBookResponse;
import store.novabook.store.book.service.LikesService;
import store.novabook.store.common.security.aop.CurrentMembers;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/store/books/likes")
public class LikesController implements LikesControllerDocs {
	private final LikesService likesService;

	@GetMapping("/member")
	public ResponseEntity<Page<GetLikeBookResponse>> getLikes(@CurrentMembers Long memberId, Pageable pageable) {
		Page<GetLikeBookResponse> responses = likesService.myLikes(memberId, pageable);
		return ResponseEntity.ok().body(responses);
	}

	@PostMapping("/{bookId}")
	public ResponseEntity<LikeBookResponse> likeButton(@CurrentMembers Long memberId, @PathVariable Long bookId) {
		LikeBookResponse response = likesService.likeButton(memberId, bookId);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/{bookId}")
	public ResponseEntity<LikeBookResponse> isLiked(@PathVariable Long bookId, @CurrentMembers Long memberId) {
		return ResponseEntity.ok().body(likesService.getLikeResponse(memberId, bookId));
	}
}
