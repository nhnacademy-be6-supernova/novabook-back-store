package store.novabook.store.book.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.store.book.controller.docs.LikesControllerDocs;
import store.novabook.store.book.dto.response.CreateLikesResponse;
import store.novabook.store.book.dto.response.GetLikeBookResponse;
import store.novabook.store.book.service.LikesService;
import store.novabook.store.common.security.aop.CurrentUser;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/store/books/likes")
public class LikesController implements LikesControllerDocs {
	private final LikesService likesService;

	@GetMapping("/member")
	public ResponseEntity<Page<GetLikeBookResponse>> getLikes(@CurrentUser Long memberId, Pageable pageable) {
		Page<GetLikeBookResponse> responses = likesService.myLikes(memberId, pageable);
		return ResponseEntity.ok().body(responses);
	}

	@PostMapping
	public ResponseEntity<CreateLikesResponse> createLikes(@CurrentUser Long memberId, @Valid @RequestParam Long bookId) {
		CreateLikesResponse createLikesResponse = likesService.createLikes(memberId, bookId);
		return ResponseEntity.status(HttpStatus.CREATED).body(createLikesResponse);
	}

	@DeleteMapping("/{likesId}")
	public ResponseEntity<Void> deleteLikes(@PathVariable Long likesId) {
		HttpStatus status = likesService.deleteLikes(likesId);
		return ResponseEntity.status(status).build();
	}
}
