package store.novabook.store.book.dto.response;

import lombok.Builder;
import store.novabook.store.book.entity.Likes;

@Builder
public record CreateLikesResponse(
	Long id) {
	public static CreateLikesResponse from(Likes likes) {
		return CreateLikesResponse.builder().id(likes.getId()).build();
	}
}