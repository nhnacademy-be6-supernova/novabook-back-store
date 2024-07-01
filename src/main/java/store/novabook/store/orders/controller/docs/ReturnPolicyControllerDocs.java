package store.novabook.store.orders.controller.docs;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import store.novabook.store.orders.dto.request.CreateReturnPolicyRequest;
import store.novabook.store.orders.dto.response.CreateResponse;
import store.novabook.store.orders.dto.response.GetReturnPolicyResponse;

@Tag(name = "ReturnPolicy API", description = "환불 정책 API")
public interface ReturnPolicyControllerDocs {

	//생성
	@Operation(summary = "환불 정책 생성", description = "환불 정책 생성합니다 ")
	ResponseEntity<CreateResponse> createReturnPolicy(@Valid @RequestBody CreateReturnPolicyRequest request);

	//전체 조회
	@Operation(summary = "환불 정책 전체 조회", description = "환불 정책 모두 조회합니다.")
	ResponseEntity<Page<GetReturnPolicyResponse>> getReturnPolicyAll();

	//단건 조회
	@Operation(summary = "환불 정책 단건 조회", description = "환불 정책 조회합니다.")
	ResponseEntity<GetReturnPolicyResponse> getReturnPolicy(@PathVariable Long id);
}
