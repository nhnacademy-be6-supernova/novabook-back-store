package store.novabook.store.image.dto.response;

import store.novabook.store.image.entity.Image;

public record CreateImageResponse(Long id) {
	public static CreateImageResponse formEntity(Image image) {
		return new CreateImageResponse(image.getId());
	}
}
