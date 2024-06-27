package store.novabook.store.category.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.category.dto.CreateCategoryRequest;
import store.novabook.store.category.dto.CreateCategoryResponse;
import store.novabook.store.category.dto.GetCategoryListResponse;
import store.novabook.store.category.dto.GetCategoryResponse;
import store.novabook.store.category.dto.SubCategoryDTO;
import store.novabook.store.category.entity.Category;
import store.novabook.store.category.repository.BookCategoryRepository;
import store.novabook.store.category.repository.CategoryRepository;
import store.novabook.store.common.exception.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {
	private final CategoryRepository categoryRepository;
	private final BookCategoryRepository bookCategoryRepository;

	public CreateCategoryResponse create(CreateCategoryRequest request) {

		Category newCategory;

		if(request.topCategoryId() == null){
			newCategory = new Category(request.name());
		}
		else {
			Category topCategory = categoryRepository.findById(request.topCategoryId())
				.orElseThrow(() -> new EntityNotFoundException(Category.class, request.topCategoryId()));
			newCategory = new Category(topCategory,request.name());
		}

		Category savedCategory = categoryRepository.save(newCategory);
		return new CreateCategoryResponse(savedCategory.getId());
	}

	@Transactional(readOnly = true)
	public GetCategoryResponse getCategory(Long id) {
		Category category = categoryRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(Category.class, id));
		return GetCategoryResponse.fromEntity(category, null);
	}


	@Transactional(readOnly = true)
	public GetCategoryListResponse getAllCategories() {
		List<Category> categories = categoryRepository.findAllByOrderByTopCategoryDesc();

		List<GetCategoryResponse> categoryDTOs = new ArrayList<>();
		for (Category category : categories) {
			if(category.getTopCategory() == null){
				GetCategoryResponse categoryDTO = convertToDTO(category);
				categoryDTOs.add(categoryDTO);
			}

		}

		return new GetCategoryListResponse(categoryDTOs);
	}

	private GetCategoryResponse convertToDTO(Category category) {
		List<SubCategoryDTO> subCategoryDTOs = new ArrayList<>();
		if (category.getSubCategories() != null) {
			for (Category subCategory : category.getSubCategories()) {
				SubCategoryDTO subCategoryDTO = new SubCategoryDTO(subCategory.getId(),subCategory.getName());
				subCategoryDTOs.add(subCategoryDTO);
			}
		}

		return GetCategoryResponse.fromEntity(category,subCategoryDTOs);
	}


	public void delete(Long id) {
		categoryRepository.deleteById(id);
	}
}
