package store.novabook.store.book.dto;

import lombok.Builder;

@Builder
public record AuthorBookDto(Long authorId, Long bookId) {
}
