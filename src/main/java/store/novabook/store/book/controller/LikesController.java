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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.store.book.dto.CreateLikesRequest;
import store.novabook.store.book.dto.CreateLikesResponse;
import store.novabook.store.book.dto.SearchBookResponse;
import store.novabook.store.book.service.LikesService;
@Tag(name = "Likes API 명세서", description = "Likes API 명세서")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/store/books/likes")
public class LikesController {
	private final LikesService likesService;

	//member id 가 좋아요 누른 책들
	@Operation(summary = "누른 좋아요", description = "좋아요를 누른 책들을 받습니다. 헤더에 memberId를 포함합니다.")
	@GetMapping("/members/books")
	public ResponseEntity<Page<SearchBookResponse>> getLikes(@RequestHeader Long memberId, Pageable pageable) {
		Page<SearchBookResponse> searchBookResponses = likesService.myLikes(memberId, pageable);
		return ResponseEntity.ok().body(searchBookResponses);
	}

	//좋아요 누르면 하기 ( 프론트에서 좋아요를 눌렀었는지 아닌지 판단
	@Operation(summary = "생성", description = "생성 합니다.")
	@PostMapping
	public ResponseEntity<CreateLikesResponse> createLikes(@Valid @RequestBody CreateLikesRequest createLikesRequest) {
		CreateLikesResponse createLikesResponse = likesService.createLikes(createLikesRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(createLikesResponse);
	}

	//좋아요 삭제
	@Operation(summary = "삭제", description = "삭제 합니다.")
	@DeleteMapping("/{likesId}")
	public ResponseEntity<Void> deleteLikes(@PathVariable Long likesId) {
		HttpStatus status = likesService.deleteLikes(likesId);
		return ResponseEntity.status(status).build();
	}

}
