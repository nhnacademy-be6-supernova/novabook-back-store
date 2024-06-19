package store.novabook.store.book.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateLikesRequest(
	@NotNull
	Long bookId,
	@NotNull
	Long memberId
) {
}