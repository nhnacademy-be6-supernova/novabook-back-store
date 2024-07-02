package store.novabook.store.category.controller.docs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import store.novabook.store.category.dto.request.CreateCategoryRequest;
import store.novabook.store.category.dto.response.CreateCategoryResponse;
import store.novabook.store.category.dto.response.GetCategoryListResponse;
import store.novabook.store.category.dto.response.GetCategoryResponse;

@Tag(name = "Category API")
public interface CategoryControllerDocs {
	@Operation(summary = "카테고리 생성", description = "카테고리를 생성합니다.")
	ResponseEntity<CreateCategoryResponse> createCategory(@Valid @RequestBody CreateCategoryRequest category);
	@Operation(summary = "카테고리 단건 조회", description = "카테고리 ID로 조회힙니다.")
	ResponseEntity<GetCategoryResponse> getCategory(@PathVariable Long id);
	@Operation(summary = "카테고리 전체 조회", description = "카테고리를 전체 조회힙니다.")
	ResponseEntity<GetCategoryListResponse> getCategoryAll();
	@Operation(summary = "카테고리 삭제", description = "카테고리를 삭제합니다.")
	ResponseEntity<Void> deleteCategory(@PathVariable Long id);
}
