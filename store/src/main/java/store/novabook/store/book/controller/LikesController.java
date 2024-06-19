package store.novabook.store.book.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import store.novabook.store.book.dto.CreateLikesRequest;
import store.novabook.store.book.dto.DeleteLikesRequest;
import store.novabook.store.book.dto.SearchBookResponse;
import store.novabook.store.book.service.LikesService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/likes")
public class LikesController {
	private final LikesService likesService;
	//member id 가 좋아요 누른 책들
	@GetMapping("/{memberId}")
	public ResponseEntity<List<SearchBookResponse>> getLikes(@PathVariable Long memberId) {
		List<SearchBookResponse> searchBookResponses = likesService.myLikes(memberId);
		return ResponseEntity.ok().body(searchBookResponses);
	}

	//좋아요 생성
	@PostMapping
	public ResponseEntity<Void> createLikes(@RequestBody CreateLikesRequest createLikesRequest) {
		likesService.createLikes(createLikesRequest);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	//좋아요 삭제
	@DeleteMapping
	public ResponseEntity<Void> deleteLikes(@RequestBody DeleteLikesRequest deleteLikesRequest) {
		likesService.deleteLikes(deleteLikesRequest);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
