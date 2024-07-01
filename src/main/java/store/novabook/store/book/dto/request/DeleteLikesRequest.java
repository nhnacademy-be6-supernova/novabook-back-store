package store.novabook.store.book.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record DeleteLikesRequest(
	@NotNull(message = "bookId 값은 필수 입니다 ")
	Long bookId,
	@NotNull(message = "memberId 값은 필수 입니다 ")
	Long memberId
) {
}