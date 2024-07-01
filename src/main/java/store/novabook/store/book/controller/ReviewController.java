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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.store.book.controller.docs.ReviewControllerDocs;
import store.novabook.store.book.dto.CreateReviewRequest;
import store.novabook.store.book.dto.CreateReviewResponse;
import store.novabook.store.book.dto.GetReviewResponse;
import store.novabook.store.book.dto.SearchBookResponse;
import store.novabook.store.book.dto.UpdateReviewRequest;
import store.novabook.store.book.service.ReviewService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/store/reviews")
public class ReviewController implements ReviewControllerDocs {

	private final ReviewService reviewService;
	private static final Long MEMBER_ID = 7L;

	@GetMapping("/members/books")
	public ResponseEntity<Page<SearchBookResponse>> getReviewedBooks( Pageable pageable) {
		Page<SearchBookResponse> searchBookResponses = reviewService.myReviews(MEMBER_ID, pageable);
		return ResponseEntity.ok(searchBookResponses);
	}

	@GetMapping("/members")
	public ResponseEntity<Page<GetReviewResponse>> getReviewByMember( Pageable pageable) {
		Page<GetReviewResponse> getReviewResponses = reviewService.membersReviews(MEMBER_ID, pageable);
		return ResponseEntity.ok(getReviewResponses);
	}

	@GetMapping("/books/{bookId}")
	public ResponseEntity<Page<GetReviewResponse>> getReviewByBookId(@PathVariable Long bookId, Pageable pageable) {
		Page<GetReviewResponse> getReviewResponses = reviewService.bookReviews(bookId, pageable);
		return ResponseEntity.ok(getReviewResponses);
	}

	@PostMapping
	public ResponseEntity<CreateReviewResponse> createReviewed(
		@Valid @RequestBody CreateReviewRequest request) {
		CreateReviewResponse createReviewResponse = reviewService.createReview(MEMBER_ID, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(createReviewResponse);
	}

	@PutMapping("/reviews/{reviewsId}")
	public ResponseEntity<Void> updateReviewed(
		@Valid @RequestBody UpdateReviewRequest request, @PathVariable Long reviewsId) {
		reviewService.updateReview(MEMBER_ID, request, reviewsId);
		return ResponseEntity.ok().build();
	}
}
