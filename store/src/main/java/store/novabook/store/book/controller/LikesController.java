package store.novabook.store.book.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	@GetMapping("/{memberId}")
	public ResponseEntity<List<SearchBookResponse>> getLikes(@PathVariable Long memberId) {
		List<SearchBookResponse> searchBookResponses = likesService.myLikes(memberId);
		return ResponseEntity.ok().body(searchBookResponses);
	}

	//좋아요 누르면 하기
	@PostMapping
	public ResponseEntity<Void> pushLikes(@RequestBody CreateLikesRequest createLikesRequest) {
		HttpStatus status = likesService.pushedLikes(createLikesRequest);
		return ResponseEntity.status(status).build();
	}

	//좋아요 삭제
}
