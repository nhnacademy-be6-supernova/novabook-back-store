package store.novabook.store.book.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import store.novabook.store.book.dto.CreateLikesRequest;
import store.novabook.store.book.dto.SearchBookResponse;
import store.novabook.store.book.service.LikesService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/likes")
public class LikesController {
	private final LikesService likesService;

	//member id 가 좋아요 누른 책들
	@GetMapping("/members/books")
	public ResponseEntity<Page<SearchBookResponse>> getLikes(@RequestHeader Long memberId, Pageable pageable) {
		Page<SearchBookResponse> searchBookResponses = likesService.myLikes(memberId, pageable);
		return ResponseEntity.ok().body(searchBookResponses);
	}

	//좋아요 누르면 하기 ( 프론트에서 좋아요를 눌렀었는지 아닌지 판단
	@PostMapping
	public ResponseEntity<Void> createLikes(@RequestBody CreateLikesRequest createLikesRequest) {
		HttpStatus status = likesService.createLikes(createLikesRequest);
		return ResponseEntity.status(status).build();
	}

	//좋아요 삭제
	@DeleteMapping("/{likesId}")
	public ResponseEntity<Void> deleteLikes(@PathVariable Long likesId) {
		HttpStatus status = likesService.deleteLikes(likesId);
		return ResponseEntity.status(status).build();
	}

}
