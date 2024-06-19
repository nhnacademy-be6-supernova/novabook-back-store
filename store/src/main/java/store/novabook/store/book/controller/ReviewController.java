package store.novabook.store.book.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import store.novabook.store.book.dto.CreateReviewRequest;
import store.novabook.store.book.dto.SearchBookResponse;
import store.novabook.store.book.dto.UpdateReviewRequest;
import store.novabook.store.book.service.ReviewService;
import store.novabook.store.point.entity.PointHistory;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class ReviewController {

	private final ReviewService reviewService;
	@GetMapping("/members/{id}/reviews")
	public ResponseEntity<List<SearchBookResponse>> getReviewedBooks(@PathVariable Long id) {
		List<SearchBookResponse> searchBookResponses = reviewService.myReviews(id);
		return ResponseEntity.ok(searchBookResponses);
	}

	//생성
	@PostMapping("/reviews")
	public ResponseEntity<Void> createReviewed(@RequestBody CreateReviewRequest request){
		reviewService.createReview(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	//수정
	@PutMapping("/reviews")
	public ResponseEntity<Void> updateReviewed(@RequestBody UpdateReviewRequest request){
		reviewService.updateReview(request);

		//todo 포인트 증가  내역에 남고 멤버에 직접 증가
		return ResponseEntity.ok().build();
	}

}
