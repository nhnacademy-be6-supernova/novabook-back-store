package store.novabook.store.category.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
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
import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.common.exception.NotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

	public static final String GET_CATEGORY_LIST_RESPONSE = "getCategoryListResponse";
	public static final String ALL_CATEGORIES = "allCategories";
	private final CategoryRepository categoryRepository;
	private final BookCategoryRepository bookCategoryRepository;
	private final CacheManager cacheManager;

	@Override
	public CreateCategoryResponse create(CreateCategoryRequest request) {
		Category newCategory;
		if (request.topCategoryId() == null) {
			newCategory = new Category(request.name());
		} else {
			Category topCategory = categoryRepository.findById(request.topCategoryId())
				.orElseThrow(() -> new NotFoundException(ErrorCode.CATEGORY_NOT_FOUND));
			newCategory = new Category(topCategory, request.name());
		}

		Category savedCategory = categoryRepository.save(newCategory);
		Objects.requireNonNull(cacheManager.getCache(GET_CATEGORY_LIST_RESPONSE)).evict(ALL_CATEGORIES);
		return new CreateCategoryResponse(savedCategory.getId());
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "categories", key = "#id")
	public GetCategoryResponse getCategory(Long id) {
		Category category = categoryRepository.findById(id)
			.orElseThrow(() -> new NotFoundException(ErrorCode.CATEGORY_NOT_FOUND));
		return GetCategoryResponse.fromEntity(category, null);
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "getCategoryListResponse", key = "'allCategories'")
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
	@Cacheable(value = "categoryCache", key = "#category.getId()")
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
		if (bookCategoryRepository.existsByCategoryId(id)) {
			Objects.requireNonNull(cacheManager.getCache("categoryCache")).evict(id);
			Objects.requireNonNull(cacheManager.getCache(GET_CATEGORY_LIST_RESPONSE)).evict(ALL_CATEGORIES);
			return new DeleteResponse(false);
		} else {

			Objects.requireNonNull(cacheManager.getCache("categoryCache")).evict(id);
			Objects.requireNonNull(cacheManager.getCache(GET_CATEGORY_LIST_RESPONSE)).evict(ALL_CATEGORIES);
			categoryRepository.deleteById(id);
			return new DeleteResponse(true);
		}

	}

	@Override
	@Transactional(readOnly = true)
	public GetCategoryIdsByBookIdResponse getCategoryIdsByBookId(Long id) {
		List<BookCategory> bookCategoryList = bookCategoryRepository.findAllByBookId(id);

		List<Long> categoryIds = new ArrayList<>();
		for (BookCategory bookCategory : bookCategoryList) {
			categoryIds.add(bookCategory.getId());
		}

		return new GetCategoryIdsByBookIdResponse(categoryIds);
	}
}
