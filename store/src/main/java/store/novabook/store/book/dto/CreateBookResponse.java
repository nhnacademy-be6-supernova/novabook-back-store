package store.novabook.store.book.dto;

import lombok.Builder;
import store.novabook.store.book.entity.Book;

@Builder
public record CreateBookResponse (Long id){
	public static CreateBookResponse fromEntity(Book book) {
		return CreateBookResponse.builder()
			.id(book.getId())
			.build();
	}
}
