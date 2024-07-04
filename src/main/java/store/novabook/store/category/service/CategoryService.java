package store.novabook.store.category.service;

import org.springframework.transaction.annotation.Transactional;

import store.novabook.store.category.dto.request.CreateCategoryRequest;
import store.novabook.store.category.dto.response.CreateCategoryResponse;
import store.novabook.store.category.dto.response.DeleteResponse;
import store.novabook.store.category.dto.response.GetCategoryIdsByBookIdResponse;
import store.novabook.store.category.dto.response.GetCategoryListResponse;
import store.novabook.store.category.dto.response.GetCategoryResponse;
import store.novabook.store.category.entity.Category;

public interface CategoryService {
	CreateCategoryResponse create(CreateCategoryRequest request);

	@Transactional(readOnly = true)
	GetCategoryResponse getCategory(Long id);

	@Transactional(readOnly = true)
	GetCategoryListResponse getAllCategories();

	GetCategoryResponse convertToDTO(Category category);

	DeleteResponse delete(Long id);

	@Transactional(readOnly = true)
	GetCategoryIdsByBookIdResponse getCategoryIdsByBookId(Long id);
}
