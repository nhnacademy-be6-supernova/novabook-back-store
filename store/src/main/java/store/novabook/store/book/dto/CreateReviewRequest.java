package store.novabook.store.book.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateReviewRequest(
	@NotNull
	Long memberId,
	@NotNull
	Long bookId,
	@NotNull
	String content,
	String image,
	int score
) {
}