package store.novabook.store.orders.dto;

import lombok.Builder;

@Builder
public record CreateResponse(Long id) {
	public static CreateResponse fromEntity(Long id) {
		return CreateResponse.builder()
			.id(id)
			.build();
	}
}
