package store.novabook.store.category.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.category.dto.SubCategoryDTO;
import store.novabook.store.category.dto.request.CreateCategoryRequest;
import store.novabook.store.category.dto.response.CreateCategoryResponse;
import store.novabook.store.category.dto.response.DeleteResponse;
import store.novabook.store.category.dto.response.GetCategoryIdsByBookIdResponse;
import store.novabook.store.category.dto.response.GetCategoryListResponse;
import store.novabook.store.category.dto.response.GetCategoryResponse;
import store.novabook.store.category.entity.BookCategory;
import store.novabook.store.category.entity.Category;
import store.novabook.store.category.repository.BookCategoryRepository;
import store.novabook.store.category.repository.CategoryRepository;
import store.novabook.store.category.service.CategoryService;
import store.novabook.store.common.exception.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepository;
	private final BookCategoryRepository bookCategoryRepository;

	@Override
	public CreateCategoryResponse create(CreateCategoryRequest request) {
		Category newCategory;

		if (request.topCategoryId() == null) {
			newCategory = new Category(request.name());
		} else {
			Category topCategory = categoryRepository.findById(request.topCategoryId())
				.orElseThrow(() -> new EntityNotFoundException(Category.class, request.topCategoryId()));
			newCategory = new Category(topCategory, request.name());
		}

		Category savedCategory = categoryRepository.save(newCategory);
		return new CreateCategoryResponse(savedCategory.getId());
	}

	@Override
	@Transactional(readOnly = true)
	public GetCategoryResponse getCategory(Long id) {
		Category category = categoryRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(Category.class, id));
		return GetCategoryResponse.fromEntity(category, null);
	}

	@Override
	@Transactional(readOnly = true)
	public GetCategoryListResponse getAllCategories() {
		List<Category> categories = categoryRepository.findAllByOrderByTopCategoryDesc();

		List<GetCategoryResponse> categoryDTOs = new ArrayList<>();
		for (Category category : categories) {
			if (category.getTopCategory() == null) {
				GetCategoryResponse categoryDTO = convertToDTO(category);
				categoryDTOs.add(categoryDTO);
			}
		}

		return new GetCategoryListResponse(categoryDTOs);
	}

	@Override
	public GetCategoryResponse convertToDTO(Category category) {
		List<SubCategoryDTO> subCategoryDTOs = new ArrayList<>();
		if (category.getSubCategories() != null) {
			for (Category subCategory : category.getSubCategories()) {
				SubCategoryDTO subCategoryDTO = new SubCategoryDTO(subCategory.getId(), subCategory.getName());
				subCategoryDTOs.add(subCategoryDTO);
			}
		}

		return GetCategoryResponse.fromEntity(category, subCategoryDTOs);
	}

	@Override
	public DeleteResponse delete(Long id) {
		if (bookCategoryRepository.existsById(id)) {
			return new DeleteResponse(false);
		} else {
			categoryRepository.deleteById(id);
			return new DeleteResponse(true);
		}
	}

	@Override
	public GetCategoryIdsByBookIdResponse getCategoryIdsByBookId(Long id) {
		List<BookCategory> bookCategoryList = bookCategoryRepository.findAllByBookId(id);

		List<Long> categoryIds = new ArrayList<>();
		for (BookCategory bookCategory : bookCategoryList) {
			categoryIds.add(bookCategory.getId());
		}

		return new GetCategoryIdsByBookIdResponse(categoryIds);
	}
}
