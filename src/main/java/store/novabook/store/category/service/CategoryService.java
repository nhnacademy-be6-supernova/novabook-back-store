package store.novabook.store.category.service;
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

	public CreateCategoryResponse create(CreateCategoryRequest category) {
		if (categoryRepository.existsByName(category.name())) {
			throw new AlreadyExistException(category.name());
		}

		Category newCategory = categoryRepository.save(new Category(category.name()));
		return new CreateCategoryResponse(newCategory.getId());
	}

	@Transactional(readOnly = true)
	public GetCategoryResponse getCategory(Long id) {
		Category category = categoryRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(Category.class, id));

		return GetCategoryResponse.fromEntity(category);
	}

	@Transactional(readOnly = true)
	public Page<GetCategoryResponse> getCategoryAll(Pageable pageable) {

		Page<Category> categories = categoryRepository.findAll(pageable);
		Page<GetCategoryResponse> categoryResponses = categories.map(GetCategoryResponse::fromEntity);

		return new PageImpl<>(categoryResponses.getContent(), pageable, categories.getTotalElements());
	}

	public void delete(Long id) {
		categoryRepository.deleteById(id);
	}
}
