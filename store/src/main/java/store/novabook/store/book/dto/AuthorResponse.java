package store.novabook.store.book.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import store.novabook.store.book.entity.Author;

@Builder
public record AuthorResponse(
	Long id,
	String name,
	String description,
	String role,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {
	public static AuthorResponse from(Author author) {
		return AuthorResponse.builder()
			.id(author.getId())
			.name(author.getName())
			.description(author.getDescription())
			.role(author.getRole())
			.createdAt(author.getCreatedAt())
			.updatedAt(author.getUpdatedAt())
			.build();
	}

}

