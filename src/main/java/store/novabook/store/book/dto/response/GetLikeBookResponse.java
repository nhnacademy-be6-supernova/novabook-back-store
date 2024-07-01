package store.novabook.store.book.dto.response;

import lombok.Builder;
import store.novabook.store.book.entity.Likes;

@Builder
public record GetLikeBookResponse(
	Long id,
	Long bookId,
	String title,
	String author,
	String publisher
) {
	public static GetLikeBookResponse from(Likes likes) {
		return GetLikeBookResponse.builder()
			.id(likes.getId())
			.bookId(likes.getBook().getId())
			.title(likes.getBook().getTitle())
			.author(likes.getBook().getAuthor())
			.publisher(likes.getBook().getPublisher())
			.build();
	}
}