package store.novabook.store.order.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import store.novabook.store.order.entity.ReturnPolicy;

@Builder
public record GetReturnPolicyResponse(
	Long id,
	String content,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {
	public static GetReturnPolicyResponse from(ReturnPolicy returnPolicy) {
		return GetReturnPolicyResponse.builder()
			.id(returnPolicy.getId())
			.content(returnPolicy.getContent())
			.createdAt(returnPolicy.getCreatedAt())
			.updatedAt(returnPolicy.getUpdatedAt())
			.build();
	}
}
