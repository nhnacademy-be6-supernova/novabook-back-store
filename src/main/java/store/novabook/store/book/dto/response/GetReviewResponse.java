package store.novabook.store.book.dto.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import store.novabook.store.book.dto.ReviewImageDto;

public record GetReviewResponse(
	String nickName,
	Long reviewId,
	Long orderBookId,
	String content,
	List<String> reviewImages,
	int score,
	LocalDateTime createdAt
) {
	public static Map<Long, GetReviewResponse> of(List<ReviewImageDto> reviewImageDtoList) {
		Map<Long, GetReviewResponse> reviewMap = new HashMap<>();
		for (ReviewImageDto dto : reviewImageDtoList) {
			reviewMap.computeIfAbsent(dto.reviewId(),
				id -> new GetReviewResponse(maskString(dto.nickName()), dto.reviewId(), dto.orderBookId(),
					dto.content(),
					new ArrayList<>(),
					dto.score(), dto.createdAt())).reviewImages().add(dto.reviewImage());
		}
		return reviewMap;
	}

	public static String maskString(String input) {
		return input != null && input.length() > 3
			? input.substring(0, 3) + "*".repeat(input.length() - 3)
			: input;
	}
}
