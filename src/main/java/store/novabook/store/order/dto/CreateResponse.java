package store.novabook.store.order.dto;

import lombok.Builder;
import store.novabook.store.book.entity.Book;

@Builder
public record CreateResponse(Long id) {
	public static CreateResponse fromEntity(Long id) {
		return CreateResponse.builder()
			.id(id)
			.build();
	}
}
