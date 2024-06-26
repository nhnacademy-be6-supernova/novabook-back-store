package store.novabook.store.category.dto;

import store.novabook.store.category.entity.Category;

public record SubCategoryDTO(Long id, String name) {
	public static SubCategoryDTO fromEntity(Category category) {
		return new SubCategoryDTO(category.getId(), category.getName());
	}
}
