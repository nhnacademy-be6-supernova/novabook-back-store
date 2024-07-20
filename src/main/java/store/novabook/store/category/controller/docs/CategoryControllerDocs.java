package store.novabook.store.category.controller.docs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import store.novabook.store.category.dto.request.CreateCategoryRequest;
import store.novabook.store.category.dto.response.CreateCategoryResponse;
import store.novabook.store.category.dto.response.DeleteResponse;
import store.novabook.store.category.dto.response.GetCategoryIdsByBookIdResponse;
import store.novabook.store.category.dto.response.GetCategoryListResponse;
import store.novabook.store.category.dto.response.GetCategoryResponse;

/**
 * 카테고리 관련 API 요청을 처리하는 컨트롤러 문서화 인터페이스입니다.
 */
@Tag(name = "Category API")
public interface CategoryControllerDocs {
	/**
	 * 새로운 카테고리를 생성합니다.
	 *
	 * @param category 카테고리 생성 요청 데이터
	 * @return 생성된 카테고리의 응답 데이터와 HTTP 상태 코드
	 */
	@Operation(summary = "카테고리 생성", description = "새로운 카테고리를 생성합니다.")
	@Parameter(description = "카테고리 생성 요청 데이터", required = true)
	ResponseEntity<CreateCategoryResponse> createCategory(@Valid @RequestBody CreateCategoryRequest category);

	/**
	 * 카테고리 ID로 카테고리를 조회합니다.
	 *
	 * @param id 카테고리 ID
	 * @return 조회된 카테고리의 응답 데이터와 HTTP 상태 코드
	 */
	@Operation(summary = "카테고리 단건 조회", description = "카테고리 ID로 카테고리를 조회합니다.")
	@Parameter(description = "카테고리 ID", required = true)
	ResponseEntity<GetCategoryResponse> getCategory(@PathVariable Long id);

	/**
	 * 모든 카테고리를 조회합니다.
	 *
	 * @return 모든 카테고리의 목록과 HTTP 상태 코드
	 */
	@Operation(summary = "카테고리 전체 조회", description = "모든 카테고리를 조회합니다.")
	ResponseEntity<GetCategoryListResponse> getCategoryAll();

	/**
	 * 특정 책에 연결된 카테고리 ID 목록을 조회합니다.
	 *
	 * @param bookId 책 ID
	 * @return 책에 연결된 카테고리 ID 목록과 HTTP 상태 코드
	 */
	@Operation(summary = "책 ID로 카테고리 조회", description = "특정 책에 연결된 카테고리 ID 목록을 조회합니다.")
	@Parameter(description = "책 ID", required = true)
	ResponseEntity<GetCategoryIdsByBookIdResponse> getCategoryByBId(@PathVariable Long bookId);

	/**
	 * 카테고리를 삭제합니다.
	 *
	 * @param id 삭제할 카테고리의 ID
	 * @return 삭제 응답 데이터와 HTTP 상태 코드
	 */
	@Operation(summary = "카테고리 삭제", description = "카테고리를 삭제합니다.")
	@Parameter(description = "삭제할 카테고리 ID", required = true)
	ResponseEntity<DeleteResponse> delete(@PathVariable Long id);
}

