package store.novabook.store.book.dto.request;

import jakarta.validation.constraints.NotNull;

public record GetBookLikeRequest(
	@NotNull
	Long memberId,

	@NotNull
	Long bookId
) {
}
