package store.novabook.store.category.dto;

import store.novabook.store.category.entity.Category;

public record GetCategoryResponse(Long id, String name) {
	public static GetCategoryResponse fromEntity(Category category) {
		return new GetCategoryResponse(category.getId(), category.getName());
	}

}
