package store.novabook.store.image.dto.request;

import jakarta.validation.constraints.NotNull;

public record CreateBookImageRequest(
	@NotNull
	Long bookId,
	@NotNull
	Long imageId
) {
}
