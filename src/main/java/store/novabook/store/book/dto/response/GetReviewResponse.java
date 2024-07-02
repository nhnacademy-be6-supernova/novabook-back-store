package store.novabook.store.book.dto.response;

import lombok.Builder;
import store.novabook.store.book.entity.Review;

@Builder
public record GetReviewResponse(
	Long id,
	Long orderBookId,
	String content,
	int score
) {
	public static GetReviewResponse from(Review review) {
		return GetReviewResponse.builder()
			.id(review.getId())
			.orderBookId(review.getOrdersBook().getId())
			.content(review.getContent())
			.score(review.getScore())
			.build();
	}
}
