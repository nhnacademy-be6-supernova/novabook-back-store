package store.novabook.store.book.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.store.book.controller.docs.ReviewControllerDocs;
import store.novabook.store.book.dto.request.CreateReviewRequest;
import store.novabook.store.book.dto.request.UpdateReviewRequest;
import store.novabook.store.book.dto.response.CreateReviewResponse;
import store.novabook.store.book.dto.response.GetOrdersBookReviewIdResponse;
import store.novabook.store.book.dto.response.GetReviewListResponse;
import store.novabook.store.book.dto.response.GetReviewResponse;
import store.novabook.store.book.dto.response.SearchBookResponse;
import store.novabook.store.book.service.ReviewService;
import store.novabook.store.book.service.impl.ReviewServiceImpl;
import store.novabook.store.common.security.aop.CurrentUser;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/store/reviews")
public class ReviewController implements ReviewControllerDocs {

	private final ReviewService reviewService;
	private static final Long MEMBER_ID = 7L;

	@GetMapping("/members/books")
	public ResponseEntity<Page<SearchBookResponse>> getReviewedBooks(@CurrentUser Long memberId, Pageable pageable) {
		Page<SearchBookResponse> searchBookResponses = reviewService.myReviews(memberId, pageable);
		return ResponseEntity.ok(searchBookResponses);
	}

	@GetMapping("/books/{bookId}")
	public ResponseEntity<GetReviewListResponse> getReviewByBookId(@PathVariable Long bookId) {
		//List dto 생성
		GetReviewListResponse getReviewListResponses = reviewService.bookReviews(bookId);
		return ResponseEntity.ok(getReviewListResponses);
	}

	@PostMapping("/{ordersBookId}")
	public ResponseEntity<CreateReviewResponse> createReviewed(
		@Valid @PathVariable Long ordersBookId,
		@Valid @RequestBody CreateReviewRequest request,
		@CurrentUser Long memberId) {
		CreateReviewResponse createReviewResponse = reviewService.createReview(ordersBookId, request, memberId);
		return ResponseEntity.status(HttpStatus.CREATED).body(createReviewResponse);
	}

	@PutMapping("/reviews/{reviewsId}")
	public ResponseEntity<Void> updateReviewed(
		@CurrentUser Long memberId,
		@Valid @RequestBody UpdateReviewRequest request, @PathVariable Long reviewsId) {
		reviewService.updateReview(memberId, request, reviewsId);
		return ResponseEntity.ok().build();
	}
}
