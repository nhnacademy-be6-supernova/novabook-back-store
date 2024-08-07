package store.novabook.store.book.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import store.novabook.store.book.entity.Review;

@Builder
public record GetOrdersBookReviewIdResponse(
	Long ordersBookId,
	Long reviewId,
	Long ordersId,
	Long bookId,
	String bookTitle,
	LocalDateTime orderAt
) {
	public static GetOrdersBookReviewIdResponse form(Review review) {
		return GetOrdersBookReviewIdResponse.builder()
			.ordersBookId(review.getOrdersBook().getId())
			.reviewId(review.getId())
			.ordersId(review.getOrdersBook().getOrders().getId())
			.bookTitle(review.getOrdersBook().getBook().getTitle())
			.bookId(review.getOrdersBook().getBook().getId())
			.orderAt(review.getOrdersBook().getOrders().getCreatedAt())
			.build();
	}
}
