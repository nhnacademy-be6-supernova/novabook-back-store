package store.novabook.store.book.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateReviewRequest(
	@NotNull(message = "memberId 값은 필수 입니다 ")
	Long memberId,
	@NotNull(message = "bookId 값은 필수 입니다 ")
	Long bookId,
	@NotBlank(message = "content 값은 필수 입니다 ")
	String content,
	String image,
	Integer score
) {
}