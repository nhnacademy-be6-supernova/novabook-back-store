package store.novabook.store.category.service;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.category.dto.CreateCategoryRequest;
import store.novabook.store.category.dto.CreateCategoryResponse;
import store.novabook.store.category.entity.Category;
import store.novabook.store.category.entity.GetCategoryResponse;
import store.novabook.store.category.repository.CategoryRepository;
import store.novabook.store.common.exception.AlreadyExistException;
import store.novabook.store.common.exception.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {
	private final CategoryRepository categoryRepository;

	public CreateCategoryResponse create(CreateCategoryRequest request) {
		if (categoryRepository.existsByName(request.name())) {
			throw new AlreadyExistException(request.name());
		}

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

	// @Transactional(readOnly = true)
	// public Page<GetCategoryResponse> getCategoryAll(Pageable pageable) {
	//
	// 	Page<Category> categories = categoryRepository.findAll(pageable);
	// 	Page<GetCategoryResponse> categoryResponses = categories.map(GetCategoryResponse::fromEntity);
	//
	// 	return new PageImpl<>(categoryResponses.getContent(), pageable, categories.getTotalElements());
	// }

	@Transactional(readOnly = true)
	public List<GetCategoryResponse> getCategoryAll() {

		List<Category> categories = categoryRepository.findAllByOrderByTopCategoryId();
		List<GetCategoryResponse> responses = new ArrayList<>();
		for (Category category : categories) {
			responses.add(GetCategoryResponse.fromEntity(category));
		}

		return responses;
	}

	public void delete(Long id) {
		categoryRepository.deleteById(id);
	}
}
