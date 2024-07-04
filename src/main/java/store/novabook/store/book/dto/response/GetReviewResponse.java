package store.novabook.store.book.dto.response;

import java.util.List;

import lombok.Builder;
import store.novabook.store.book.entity.Review;
import store.novabook.store.image.entity.ReviewImage;

@Builder
public record GetReviewResponse(
	Long id,
	Long orderBookId,
	String content,
	List<String> reviewImages,
	int score
) {
	public static GetReviewResponse from(Review review, List<String> reviewImages) {
		return GetReviewResponse.builder()
			.id(review.getId())
			.orderBookId(review.getOrdersBook().getId())
			.content(review.getContent())
			.score(review.getScore())
			.reviewImages(reviewImages)
			.build();
	}
}
