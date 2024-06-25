package store.novabook.store.category.controller;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.store.category.dto.CreateCategoryRequest;
import store.novabook.store.category.dto.CreateCategoryResponse;
import store.novabook.store.category.dto.GetCategoryListResponse;
import store.novabook.store.category.dto.GetCategoryResponse;
import store.novabook.store.category.service.CategoryService;
@Tag(name = "Category API 명세서", description = "Category API 명세서")
@RestController
@RequestMapping("/api/v1/store/categories")
@RequiredArgsConstructor
public class CategoryController {
	private final CategoryService categoryService;

	@Operation(summary = "카테고리 생성", description = "카테고리를 생성합니다.")
	@PostMapping
	public ResponseEntity<CreateCategoryResponse> createCategory(@Valid @RequestBody CreateCategoryRequest category) {
		return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.create(category));
	}

	@Operation(summary = "카테고리 단건 조회", description = "카테고리 ID로 조회힙니다.")
	@GetMapping("/{id}")
	public ResponseEntity<GetCategoryResponse> getCategory(@PathVariable Long id) {
		return ResponseEntity.ok().body(categoryService.getCategory(id));
	}

	@Operation(summary = "카테고리 전체 조회", description = "카테고리 ID로 조회힙니다.")
	@GetMapping
	public ResponseEntity<List<GetCategoryListResponse>> getCategoryAll() {
		return ResponseEntity.ok().body(categoryService.getCategoryAll());
	}



	@Operation(summary = "카테고리 삭제", description = "카테고리를 삭제합니다.")
	@DeleteMapping("/{id}")
	@CrossOrigin(origins = "http://localhost:8080")
	public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
		categoryService.delete(id);
		return ResponseEntity.ok().build();
	}

}
