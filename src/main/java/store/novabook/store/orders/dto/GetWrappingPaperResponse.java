package store.novabook.store.orders.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import store.novabook.store.orders.entity.WrappingPaper;

@Builder
public record GetWrappingPaperResponse(
	Long id,
	long price,
	String status,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {
	public static GetWrappingPaperResponse from(WrappingPaper wrappingPaper) {
		return GetWrappingPaperResponse.builder()
			.id(wrappingPaper.getId())
			.price(wrappingPaper.getPrice())
			.status(wrappingPaper.getStatus())
			.createdAt(wrappingPaper.getCreatedAt())
			.updatedAt(wrappingPaper.getUpdatedAt())
			.build();
	}
}
