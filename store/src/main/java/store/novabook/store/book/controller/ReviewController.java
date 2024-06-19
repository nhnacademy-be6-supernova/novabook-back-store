package store.novabook.store.book.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import store.novabook.store.book.dto.CreateReviewRequest;
import store.novabook.store.book.dto.GetReviewResponse;
import store.novabook.store.book.dto.SearchBookResponse;
import store.novabook.store.book.dto.UpdateReviewRequest;
import store.novabook.store.book.service.ReviewService;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class ReviewController {

	private final ReviewService reviewService;

	@GetMapping("/reviews/members/{id}")
	public ResponseEntity<Page<SearchBookResponse>> getReviewedBooks(@PathVariable Long id, Pageable pageable) {
		Page<SearchBookResponse> searchBookResponses = reviewService.myReviews(id, pageable);
		return ResponseEntity.ok(searchBookResponses);
	}

	@GetMapping("/reviews/members/{memberId}")
	public ResponseEntity<Page<GetReviewResponse>> getReviewByMember(@PathVariable Long memberId, Pageable pageable) {
		Page<GetReviewResponse> getReviewResponses = reviewService.membersReviews(memberId, pageable);
		return ResponseEntity.ok(getReviewResponses);
	}

	@GetMapping("/reviews/books/{bookId}")
	public ResponseEntity<Page<GetReviewResponse>> getReviewByBookId(@PathVariable Long bookId, Pageable pageable) {
		Page<GetReviewResponse> getReviewResponses = reviewService.bookReviews(bookId, pageable);
		return ResponseEntity.ok(getReviewResponses);
	}

	//생성
	@PostMapping("/reviews")
	public ResponseEntity<Void> createReviewed(@RequestBody CreateReviewRequest request) {
		reviewService.createReview(request);
		//todo 포인트 증가  내역에 남고 멤버에 직접 증가
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	//수정
	@PutMapping("/reviews/{reviewsId}")
	public ResponseEntity<Void> updateReviewed(@RequestBody UpdateReviewRequest request, @PathVariable Long reviewsId) {
		reviewService.updateReview(request, reviewsId);
		return ResponseEntity.ok().build();
	}

}
