package store.novabook.store.book.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.store.book.controller.docs.ReviewControllerDocs;
import store.novabook.store.book.dto.request.CreateReviewRequest;
import store.novabook.store.book.dto.request.UpdateReviewRequest;
import store.novabook.store.book.dto.response.CreateReviewResponse;
import store.novabook.store.book.dto.response.GetReviewListResponse;
import store.novabook.store.book.dto.response.GetReviewResponse;
import store.novabook.store.book.service.ReviewService;
import store.novabook.store.common.security.aop.CurrentUser;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/store/reviews")
public class ReviewController implements ReviewControllerDocs {

	private final ReviewService reviewService;

	@GetMapping("/{reviewId}")
	public ResponseEntity<GetReviewResponse> getReview(@PathVariable Long reviewId) {
		GetReviewResponse response = reviewService.getReviewById(reviewId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/books/{bookId}")
	public ResponseEntity<GetReviewListResponse> getReviewByBookId(@PathVariable Long bookId) {
		//List dto 생성
		GetReviewListResponse getReviewListResponses = reviewService.bookReviews(bookId);
		return ResponseEntity.ok(getReviewListResponses);
	}

	@PostMapping("/{ordersBookId}")
	public ResponseEntity<CreateReviewResponse> createReviewed(@PathVariable Long ordersBookId,
		@Valid @RequestBody CreateReviewRequest request, @CurrentUser Long memberId) {
		CreateReviewResponse createReviewResponse = reviewService.createReview(ordersBookId, request, memberId);
		return ResponseEntity.status(HttpStatus.CREATED).body(createReviewResponse);
	}

	@PutMapping("/reviews/{reviewsId}")
	public ResponseEntity<Void> updateReviewed(@Valid @RequestBody UpdateReviewRequest request,
		@PathVariable Long reviewsId) {
		reviewService.updateReview(request, reviewsId);
		return ResponseEntity.ok().build();
	}
}
