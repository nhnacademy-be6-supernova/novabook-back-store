package store.novabook.store.category.entity;

import lombok.Builder;

@Builder
public record GetCategoryResponse(Long id, String name) {
	public static GetCategoryResponse fromEntity(Category category) {
		return GetCategoryResponse.builder().name(category.getName()).id(category.getId()).build();
	}
}
