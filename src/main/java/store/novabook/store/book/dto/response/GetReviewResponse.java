package store.novabook.store.book.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record GetReviewResponse(
	String nickName,
	Long reviewId,
	Long orderBookId,
	String content,
	List<String> reviewImages,
	int score,
	LocalDateTime createdAt
) {
}
