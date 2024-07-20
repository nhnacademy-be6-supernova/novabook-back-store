package store.novabook.store.orders.controller.docs;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import store.novabook.store.orders.dto.request.CreateReturnPolicyRequest;
import store.novabook.store.orders.dto.response.CreateResponse;
import store.novabook.store.orders.dto.response.GetReturnPolicyResponse;

/**
 * 환불 정책 API 문서화 인터페이스입니다.
 * 이 인터페이스는 환불 정책의 생성, 전체 조회 및 단건 조회 기능을 문서화합니다.
 */
@Tag(name = "ReturnPolicy API", description = "환불 정책 API")
public interface ReturnPolicyControllerDocs {

	/**
	 * 새로운 환불 정책을 생성합니다.
	 *
	 * @param request 생성할 환불 정책 정보
	 * @return 생성된 환불 정책에 대한 응답
	 */
	@Operation(summary = "환불 정책 생성", description = "새로운 환불 정책을 생성합니다.")
	@Parameter(name = "request", description = "생성할 환불 정책 정보", required = true)
	ResponseEntity<CreateResponse> createReturnPolicy(@Valid @RequestBody CreateReturnPolicyRequest request);

	/**
	 * 모든 환불 정책을 조회합니다.
	 *
	 * @return 전체 환불 정책에 대한 응답
	 */
	@Operation(summary = "환불 정책 전체 조회", description = "모든 환불 정책을 조회합니다.")
	ResponseEntity<Page<GetReturnPolicyResponse>> getReturnPolicyAll();

	/**
	 * 환불 정책 ID로 환불 정책 정보를 조회합니다.
	 *
	 * @param id 환불 정책 ID
	 * @return 환불 정책에 대한 응답
	 */
	@Operation(summary = "환불 정책 단건 조회", description = "환불 정책 ID로 환불 정책 정보를 조회합니다.")
	@Parameter(name = "id", description = "환불 정책 ID", required = true)
	ResponseEntity<GetReturnPolicyResponse> getReturnPolicy(@PathVariable Long id);
}
