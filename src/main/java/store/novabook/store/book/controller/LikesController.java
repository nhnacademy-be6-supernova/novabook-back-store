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
import store.novabook.store.book.dto.CreateLikesResponse;
import store.novabook.store.book.dto.GetLikeBookResponse;
import store.novabook.store.book.service.LikesService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/store/books/likes")
public class LikesController implements LikesControllerDocs {
	private final LikesService likesService;

	private static final Long MEMBER_ID = 7L;

	@GetMapping("/member")
	public ResponseEntity<Page<GetLikeBookResponse>> getLikes(Pageable pageable) {
		Page<GetLikeBookResponse> responses = likesService.myLikes(MEMBER_ID, pageable);
		return ResponseEntity.ok().body(responses);
	}

	@PostMapping
	public ResponseEntity<CreateLikesResponse> createLikes(@Valid @RequestParam Long bookId) {
		CreateLikesResponse createLikesResponse = likesService.createLikes(MEMBER_ID, bookId);
		return ResponseEntity.status(HttpStatus.CREATED).body(createLikesResponse);
	}

	@DeleteMapping("/{likesId}")
	public ResponseEntity<Void> deleteLikes(@PathVariable Long likesId) {
		HttpStatus status = likesService.deleteLikes(likesId);
		return ResponseEntity.status(status).build();
	}
}
