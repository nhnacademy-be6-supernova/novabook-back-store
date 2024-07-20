package store.novabook.store.tag.controller.docs;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import store.novabook.store.tag.dto.request.CreateTagRequest;
import store.novabook.store.tag.dto.request.UpdateTagRequest;
import store.novabook.store.tag.dto.response.CreateTagResponse;
import store.novabook.store.tag.dto.response.GetTagListResponse;
import store.novabook.store.tag.dto.response.GetTagResponse;

/**
 * 태그 관련 API 문서화 인터페이스입니다.
 * 이 인터페이스는 태그의 생성, 수정, 삭제 및 조회 기능을 문서화합니다.
 */
@Tag(name = "Tag API", description = "태그 API입니다.")
public interface TagControllerDocs {
	/**
	 * 모든 태그를 페이지네이션으로 조회합니다.
	 *
	 * @param pageable 페이지네이션 정보
	 * @return 태그 목록과 페이지 정보
	 */
	@Operation(summary = "태그 전체 조회 (페이지네이션)", description = "모든 태그를 페이지네이션으로 조회합니다.")
	@Parameter(name = "pageable", description = "페이지네이션 정보", required = true)
	ResponseEntity<Page<GetTagResponse>> getTagAll(Pageable pageable);

	/**
	 * 모든 태그를 리스트 형태로 조회합니다.
	 *
	 * @return 태그 목록
	 */
	@Operation(summary = "태그 전체 조회 (리스트)", description = "모든 태그를 리스트 형태로 조회합니다.")
	ResponseEntity<GetTagListResponse> getTagAllList();

	/**
	 * 태그 ID로 태그를 조회합니다.
	 *
	 * @param id 조회할 태그 ID
	 * @return 태그 정보
	 */
	@Operation(summary = "태그 단건 조회", description = "태그 ID로 태그를 조회합니다.")
	@Parameter(name = "id", description = "조회할 태그 ID", required = true)
	ResponseEntity<GetTagResponse> getTag(@PathVariable Long id);

	/**
	 * 새로운 태그를 생성합니다.
	 *
	 * @param createTagRequest 생성할 태그 정보
	 * @return 생성된 태그 정보
	 */
	@Operation(summary = "태그 생성", description = "새로운 태그를 생성합니다.")
	@Parameter(name = "createTagRequest", description = "생성할 태그 정보", required = true)
	ResponseEntity<CreateTagResponse> createTag(@RequestBody @Valid CreateTagRequest createTagRequest);

	/**
	 * 기존 태그를 수정합니다.
	 *
	 * @param updateTagRequest 수정할 태그 정보
	 * @param id 수정할 태그 ID
	 * @return 성공 여부
	 */
	@Operation(summary = "태그 수정", description = "기존 태그를 수정합니다.")
	@Parameter(name = "updateTagRequest", description = "수정할 태그 정보", required = true)
	@Parameter(name = "id", description = "수정할 태그 ID", required = true)
	ResponseEntity<Void> updateTag(@Valid @RequestBody UpdateTagRequest updateTagRequest, @PathVariable Long id);

	/**
	 * 태그를 삭제합니다.
	 *
	 * @param id 삭제할 태그 ID
	 * @return 성공 여부
	 */
	@Operation(summary = "태그 삭제", description = "태그를 삭제합니다.")
	@Parameter(name = "id", description = "삭제할 태그 ID", required = true)
	ResponseEntity<Void> deleteTag(@PathVariable Long id);
}
