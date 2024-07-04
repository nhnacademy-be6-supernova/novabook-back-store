package store.novabook.store.book.dto;

import java.time.LocalDateTime;

public record ReviewImageDto(
	Long reviewId,
	Long orderBookId,
	String content,
	String reviewImage,
	int score,
	LocalDateTime createdAt
) {

}
