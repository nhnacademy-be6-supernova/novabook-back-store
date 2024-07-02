package store.novabook.store.book.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import store.novabook.store.book.dto.request.CreateReviewRequest;
import store.novabook.store.book.dto.request.UpdateReviewRequest;
import store.novabook.store.book.dto.response.CreateReviewResponse;
import store.novabook.store.book.dto.response.GetOrdersBookReviewIdResponse;
import store.novabook.store.book.dto.response.GetReviewResponse;
import store.novabook.store.book.dto.response.SearchBookResponse;

public interface ReviewService {
	@Transactional(readOnly = true)
	Page<SearchBookResponse> myReviews(Long memberId, Pageable pageable);

	@Transactional(readOnly = true)
	Page<GetReviewResponse> membersReviews(Long memberId, Pageable pageable);

	@Transactional(readOnly = true)
	Page<GetReviewResponse> bookReviews(Long bookId, Pageable pageable);

	@Transactional(readOnly = true)
	Page<GetOrdersBookReviewIdResponse> getOrdersBookReviewIds(Long memberId, Pageable pageable);

	CreateReviewResponse createReview(Long orderId, CreateReviewRequest request);

	void updateReview(Long ordersId, UpdateReviewRequest request, Long reviewId);
}
