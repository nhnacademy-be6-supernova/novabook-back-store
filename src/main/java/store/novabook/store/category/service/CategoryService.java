package store.novabook.store.category.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.category.dto.CreateCategoryRequest;
import store.novabook.store.category.dto.CreateCategoryResponse;
import store.novabook.store.category.dto.GetCategoryListResponse;
import store.novabook.store.category.dto.GetCategoryResponse;
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

		return GetCategoryResponse.fromEntity(category);
	}

	@Transactional(readOnly = true)
	public List<GetCategoryListResponse> getCategoryAll() {
		List<Category> categories = categoryRepository.findAllByOrderByTopCategoryDesc();

		// 해시맵을 사용하여 카테고리를 그룹화
		Map<GetCategoryResponse, List<GetCategoryResponse>> categoryDTOMap = new HashMap<>();

		for (Category category : categories) {
			GetCategoryResponse categoryDTO = GetCategoryResponse.fromEntity(category);
			if (category.hasTopCategory()) {
				GetCategoryResponse topCategoryDTO = GetCategoryResponse.fromEntity(category.getTopCategory());
				categoryDTOMap.computeIfAbsent(topCategoryDTO, k -> new ArrayList<>()).add(categoryDTO);
			} else {
				categoryDTOMap.computeIfAbsent(categoryDTO, k -> new ArrayList<>());
			}
		}

		// GetCategoryListResponse 리스트 생성
		List<GetCategoryListResponse> getCategoryResponses = new ArrayList<>();
		for (Map.Entry<GetCategoryResponse, List<GetCategoryResponse>> entry : categoryDTOMap.entrySet()) {
			getCategoryResponses.add(new GetCategoryListResponse(entry.getKey(), entry.getValue()));
		}

		return getCategoryResponses;
	}


	public void delete(Long id) {
		// if(bookCategoryRepository.existsById(id)) {
		// 	throw new NotDeleteCategoryException();
		// }
		categoryRepository.deleteById(id);
	}
}
