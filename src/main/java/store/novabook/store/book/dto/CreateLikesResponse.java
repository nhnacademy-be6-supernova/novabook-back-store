package store.novabook.store.book.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import store.novabook.store.book.entity.Likes;

@Builder
public record CreateLikesResponse(
	@NotNull
	Long id) {
	public static CreateLikesResponse from(Likes likes) {
		return CreateLikesResponse.builder().id(likes.getId()).build();
	}
}