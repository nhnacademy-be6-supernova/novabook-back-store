package store.novabook.store.book.dto.response;

import lombok.Builder;
import store.novabook.store.book.entity.Review;

@Builder
public record CreateReviewResponse(
	Long id) {
	public static CreateReviewResponse from(Review review) {
		return CreateReviewResponse.builder().id(review.getId()).build();
	}
}
