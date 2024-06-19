package store.novabook.store.book.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record PushLikesRequest(
	@NotNull
	Long bookId,
	@NotNull
	Long memberId
) {
}