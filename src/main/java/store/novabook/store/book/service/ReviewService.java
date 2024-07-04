package store.novabook.store.book.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import store.novabook.store.book.dto.request.CreateReviewRequest;
import store.novabook.store.book.dto.request.UpdateReviewRequest;
import store.novabook.store.book.dto.response.CreateReviewResponse;
import store.novabook.store.book.dto.response.GetOrdersBookReviewIdResponse;
import store.novabook.store.book.dto.response.GetReviewListResponse;
import store.novabook.store.book.dto.response.GetReviewResponse;
import store.novabook.store.book.dto.response.SearchBookResponse;

public interface ReviewService {
	@Transactional(readOnly = true)
	Page<SearchBookResponse> myReviews(Long memberId, Pageable pageable);

	@Transactional(readOnly = true)
	GetReviewListResponse bookReviews(Long bookId);

	@Transactional(readOnly = true)
	Page<GetOrdersBookReviewIdResponse> getOrdersBookReviewIds(Long memberId, Pageable pageable);

	CreateReviewResponse createReview(Long orderId, CreateReviewRequest request, Long memberId);

	void updateReview(Long ordersId, UpdateReviewRequest request, Long reviewId);
}
