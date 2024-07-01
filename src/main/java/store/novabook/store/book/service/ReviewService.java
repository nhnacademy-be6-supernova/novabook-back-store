package store.novabook.store.book.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import store.novabook.store.book.dto.CreateReviewRequest;
import store.novabook.store.book.dto.CreateReviewResponse;
import store.novabook.store.book.dto.GetReviewResponse;
import store.novabook.store.book.dto.SearchBookResponse;
import store.novabook.store.book.dto.UpdateReviewRequest;

public interface ReviewService {
	@Transactional(readOnly = true)
	Page<SearchBookResponse> myReviews(Long memberId, Pageable pageable);

	@Transactional(readOnly = true)
	Page<GetReviewResponse> membersReviews(Long memberId, Pageable pageable);

	@Transactional(readOnly = true)
	Page<GetReviewResponse> bookReviews(Long bookId, Pageable pageable);

	CreateReviewResponse createReview(Long orderId, CreateReviewRequest request);

	boolean existsByBookIdAndMemberId(Long memberId, CreateReviewRequest request);

	void updateReview(Long ordersId, UpdateReviewRequest request, Long reviewId);
}
