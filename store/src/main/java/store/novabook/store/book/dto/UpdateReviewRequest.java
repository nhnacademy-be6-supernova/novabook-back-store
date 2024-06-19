package store.novabook.store.book.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateReviewRequest(
	@NotNull
	Long memberId,
	@NotNull
	Long bookId,
	@NotNull
	String content,
	String image,
	int score,
	LocalDateTime updatedAt
) {
}