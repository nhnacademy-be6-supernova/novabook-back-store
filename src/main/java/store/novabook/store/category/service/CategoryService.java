package store.novabook.store.category.service;

import org.springframework.transaction.annotation.Transactional;

import store.novabook.store.category.dto.CreateCategoryRequest;
import store.novabook.store.category.dto.CreateCategoryResponse;
import store.novabook.store.category.dto.GetCategoryListResponse;
import store.novabook.store.category.dto.GetCategoryResponse;
import store.novabook.store.category.entity.Category;

public interface CategoryService {
	CreateCategoryResponse create(CreateCategoryRequest request);

	@Transactional(readOnly = true)
	GetCategoryResponse getCategory(Long id);

	@Transactional(readOnly = true)
	GetCategoryListResponse getAllCategories();

	GetCategoryResponse convertToDTO(Category category);

	void delete(Long id);
}
