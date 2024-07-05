package store.novabook.store.book.dto.request;

import jakarta.validation.constraints.NotNull;

public record LikeBookRequest(
	@NotNull
	Long bookId
) {
}
