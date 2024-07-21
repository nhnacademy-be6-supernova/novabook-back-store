package store.novabook.store.orders.controller.docs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import store.novabook.store.orders.dto.request.CreateWrappingPaperRequest;
import store.novabook.store.orders.dto.request.UpdateWrappingPaperRequest;
import store.novabook.store.orders.dto.response.CreateResponse;
import store.novabook.store.orders.dto.response.GetWrappingPaperAllResponse;
import store.novabook.store.orders.dto.response.GetWrappingPaperResponse;

/**
 * 포장지 API 문서화 인터페이스입니다.
 * 이 인터페이스는 포장지의 생성, 전체 조회, 단건 조회 및 수정을 문서화합니다.
 */
@Tag(name = "WrappingPaper API", description = "포장지 API")
public interface WrappingPaperControllerDocs {
	/**
	 * 새로운 포장지를 생성합니다.
	 *
	 * @param request 생성할 포장지 정보
	 * @return 생성된 포장지에 대한 응답
	 */
	@Operation(summary = "포장지 생성", description = "새로운 포장지를 추가합니다.")
	@Parameter(name = "request", description = "생성할 포장지 정보", required = true)
	ResponseEntity<CreateResponse> createWrappingPaper(@Valid @RequestBody CreateWrappingPaperRequest request);

	/**
	 * 모든 포장지를 조회합니다.
	 *
	 * @return 포장지 목록에 대한 응답
	 */
	@Operation(summary = "포장지 전체 조회", description = "모든 포장지를 조회합니다.")
	ResponseEntity<GetWrappingPaperAllResponse> getWrappingPaperAllList();

	/**
	 * 특정 포장지 ID로 포장지 정보를 조회합니다.
	 *
	 * @param id 포장지 ID
	 * @return 포장지에 대한 응답
	 */
	@Operation(summary = "포장지 단건 조회", description = "포장지 ID로 포장지 정보를 조회합니다.")
	@Parameter(name = "id", description = "포장지 ID", required = true)
	ResponseEntity<GetWrappingPaperResponse> getWrappingPaper(@PathVariable Long id);

	/**
	 * 특정 포장지 ID로 포장지 정보를 수정합니다.
	 *
	 * @param id 포장지 ID
	 * @param request 수정할 포장지 정보
	 * @return 수정 결과
	 */
	@Operation(summary = "포장지 수정", description = "포장지 ID로 포장지 정보를 수정합니다.")
	@Parameter(name = "id", description = "포장지 ID", required = true)
	ResponseEntity<Void> updateWrappingPaper(@PathVariable Long id,
		@Valid @RequestBody UpdateWrappingPaperRequest request);
}
