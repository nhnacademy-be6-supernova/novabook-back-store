package store.novabook.store.book.dto.request;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdateReviewRequest(
	@NotNull(message = "bookId 값은 필수 입니다 ")
	Long bookId,
	@NotNull(message = "content 값은 필수 입니다 ")
	String content,
	Integer score
) {
}