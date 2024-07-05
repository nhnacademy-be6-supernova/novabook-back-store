package store.novabook.store.book.dto.response;

import lombok.Builder;

@Builder
public record LikeBookResponse(
	Boolean isLiked
) {
}
