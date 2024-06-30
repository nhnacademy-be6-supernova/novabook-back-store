package store.novabook.store.book.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateLikesRequest(
	@NotNull(message = "bookId 값은 필수 입니다 ")
	Long bookI
) {
}