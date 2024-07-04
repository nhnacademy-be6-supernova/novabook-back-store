package store.novabook.store.book.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import store.novabook.store.book.entity.Review;
import store.novabook.store.image.entity.ReviewImage;

@Builder
public record GetReviewResponse(
	Long reviewId,
	Long orderBookId,
	String content,
	LocalDateTime createdAt,
	List<String> reviewImages,
	int score
) {
}
