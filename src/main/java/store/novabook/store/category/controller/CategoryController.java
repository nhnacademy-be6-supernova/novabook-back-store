package store.novabook.store.category.controller;


import java.util.List;

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
import store.novabook.store.category.dto.GetCategoryListResponse;
import store.novabook.store.category.dto.GetCategoryResponse;
import store.novabook.store.category.service.CategoryService;

@RestController
@RequestMapping("/api/v1/store/categories")
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
	public ResponseEntity<List<GetCategoryListResponse>> getCategoryAll() {
		return ResponseEntity.ok().body(categoryService.getCategoryAll());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
		categoryService.delete(id);
		return ResponseEntity.ok().build();
	}

}
