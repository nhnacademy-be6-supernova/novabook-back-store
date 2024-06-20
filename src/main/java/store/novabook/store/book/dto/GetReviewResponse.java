package store.novabook.store.book.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import store.novabook.store.book.entity.Review;

@Builder
public record GetReviewResponse(
	@NotNull
	Long id,
	@NotNull(message = "memberId 값은 필수 입니다")
	Long memberId,
	@NotNull
	Long bookId,
	@NotNull
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
