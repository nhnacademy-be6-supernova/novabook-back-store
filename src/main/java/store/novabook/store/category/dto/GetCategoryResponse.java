package store.novabook.store.category.dto;

import java.util.List;

import store.novabook.store.category.entity.Category;

public record GetCategoryResponse(Long id, String name, List<SubCategoryDTO> sub) {
	public static GetCategoryResponse fromEntity(Category category, List<SubCategoryDTO> sub) {
		return new GetCategoryResponse(category.getId(), category.getName(), sub);
	}
}
