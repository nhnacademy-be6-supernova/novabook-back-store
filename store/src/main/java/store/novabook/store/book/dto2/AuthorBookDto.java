package store.novabook.store.book.dto2;

import lombok.Builder;

@Builder
public record AuthorBookDto(Long authorId, Long bookId) {
}
