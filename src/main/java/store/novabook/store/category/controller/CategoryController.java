package store.novabook.store.category.controller;

import static org.springframework.web.servlet.function.ServerResponse.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.store.category.dto.CreateCategoryRequest;
import store.novabook.store.category.dto.CreateCategoryResponse;
import store.novabook.store.category.entity.GetCategoryResponse;
import store.novabook.store.category.service.CategoryService;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
	private final CategoryService categoryService;

	@PostMapping
	public ResponseEntity<CreateCategoryResponse> createCategory(@Valid @RequestBody CreateCategoryRequest category) {
		return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.create(category));
	}

	@GetMapping("/{id}")
	public ResponseEntity<GetCategoryResponse> getCategory(@PathVariable Long id) {
		return ResponseEntity.ok().body(categoryService.getCategory(id));
	}

	@GetMapping
	public ResponseEntity<Page<GetCategoryResponse>> getCategoryAll(Pageable pageable) {
		return ResponseEntity.ok().body(categoryService.getCategoryAll(pageable));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
		categoryService.delete(id);
		return ResponseEntity.ok().build();
	}

}
