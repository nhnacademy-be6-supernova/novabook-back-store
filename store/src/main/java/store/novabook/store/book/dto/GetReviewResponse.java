package store.novabook.store.book.dto;

import lombok.Builder;
import store.novabook.store.book.entity.Review;

@Builder
public record GetReviewResponse(
	Long id,
	Long memberId,
	Long bookId,
	String content,
	String image,
	int score
) {
	public static GetReviewResponse from(Review review) {
		return GetReviewResponse.builder()
			.id(review.getId())
			.bookId(review.getBook().getId())
			.memberId(review.getMember().getId())
			.content(review.getContent())
			.image(review.getImage())
			.score(review.getScore())
			.build();
	}
}
