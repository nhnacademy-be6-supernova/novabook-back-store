package store.novabook.store.book.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import store.novabook.store.book.entity.Review;
import store.novabook.store.image.entity.ReviewImage;

public record GetReviewResponse(
	Long reviewId,
	Long orderBookId,
	String content,
	List<String> reviewImages,
	int score,
	LocalDateTime createdAt
) {
}
