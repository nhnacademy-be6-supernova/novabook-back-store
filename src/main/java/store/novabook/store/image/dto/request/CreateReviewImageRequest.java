package store.novabook.store.image.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateReviewImageRequest (
	@NotNull(message = "reviewId 는 필수 입력 값입니다.")
	Long reviewId,
	@NotNull(message = "imageId 는 필수 입력 값입니다.")
	Long imageId
) {
}
