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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.store.book.dto.CreateReviewRequest;
import store.novabook.store.book.dto.CreateReviewResponse;
import store.novabook.store.book.dto.GetReviewResponse;
import store.novabook.store.book.dto.SearchBookResponse;
import store.novabook.store.book.dto.UpdateReviewRequest;
import store.novabook.store.book.service.ReviewService;

@Tag(name = "Review API 명세서", description = "Review API 명세서")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/store/reviews")
public class ReviewController {

	private final ReviewService reviewService;

	@Operation(summary = "리뷰를 작성한 책들", description = "리뷰를 작성한 책들을 받습니다. 헤더에 memberId를 포함합니다.")
	@GetMapping("/members/books")
	public ResponseEntity<Page<SearchBookResponse>> getReviewedBooks(@RequestHeader Long memberId, Pageable pageable) {
		Page<SearchBookResponse> searchBookResponses = reviewService.myReviews(memberId, pageable);
		return ResponseEntity.ok(searchBookResponses);
	}

	@Operation(summary = " 작성한 리뷰들", description = "작성한 리뷰들을 받습니다. 헤더에 memberId를 포함합니다.")
	@GetMapping("/members")
	public ResponseEntity<Page<GetReviewResponse>> getReviewByMember(@RequestHeader Long memberId, Pageable pageable) {
		Page<GetReviewResponse> getReviewResponses = reviewService.membersReviews(memberId, pageable);
		return ResponseEntity.ok(getReviewResponses);
	}

	@Operation(summary = " 책에 작성된 리뷰들", description = "책에 작성된 리뷰들을 받습니다.")
	@GetMapping("/books/{bookId}")
	public ResponseEntity<Page<GetReviewResponse>> getReviewByBookId(@PathVariable Long bookId, Pageable pageable) {
		Page<GetReviewResponse> getReviewResponses = reviewService.bookReviews(bookId, pageable);
		return ResponseEntity.ok(getReviewResponses);
	}

	//생성
	@Operation(summary = " 리뷰작성 ", description = "리뷰를 작성합니다. 헤더에 memberId를 포함합니다. ")
	@PostMapping
	public ResponseEntity<CreateReviewResponse> createReviewed(@RequestHeader Long memberId,@Valid @RequestBody CreateReviewRequest request) {
		CreateReviewResponse createReviewResponse = reviewService.createReview(memberId, request);
		//todo 포인트 증가  내역에 남고 멤버에 직접 증가
		return ResponseEntity.status(HttpStatus.CREATED).body(createReviewResponse);
	}

	//수정

	@Operation(summary = " 리뷰작성 ", description = "리뷰를 작성합니다. 헤더에 memberId를 포함합니다.")
	@PutMapping("/reviews/{reviewsId}")
	public ResponseEntity<Void> updateReviewed(@RequestHeader Long memberId, @Valid @RequestBody UpdateReviewRequest request,
		@PathVariable Long reviewsId) {
		reviewService.updateReview(memberId,request, reviewsId);
		return ResponseEntity.ok().build();
	}

}
