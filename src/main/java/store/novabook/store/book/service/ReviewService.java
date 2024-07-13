package store.novabook.store.book.service;

import org.springframework.transaction.annotation.Transactional;

import store.novabook.store.book.dto.request.CreateReviewRequest;
import store.novabook.store.book.dto.request.UpdateReviewRequest;
import store.novabook.store.book.dto.response.CreateReviewResponse;
import store.novabook.store.book.dto.response.GetReviewListResponse;
import store.novabook.store.book.dto.response.GetReviewResponse;

public interface ReviewService {

	@Transactional(readOnly = true)
	GetReviewListResponse bookReviews(Long bookId);

	@Transactional(readOnly = true)
	GetReviewResponse getReviewById(Long reviewId);

	CreateReviewResponse createReview(Long orderId, CreateReviewRequest request, Long memberId);

	void updateReview(UpdateReviewRequest request, Long reviewId);
}
