package store.novabook.store.orders.controller.docs;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import store.novabook.store.orders.dto.request.CreateWrappingPaperRequest;
import store.novabook.store.orders.dto.request.UpdateWrappingPaperRequest;
import store.novabook.store.orders.dto.response.CreateResponse;
import store.novabook.store.orders.dto.response.GetWrappingPaperAllResponse;
import store.novabook.store.orders.dto.response.GetWrappingPaperResponse;

@Tag(name = "WrappingPaper API", description = "포장지 API")
public interface WrappingPaperControllerDocs {

	@Operation(summary = "포장지 생성", description = "포장지를 추가 합니다.")
	ResponseEntity<CreateResponse> createWrappingPaper(@RequestBody CreateWrappingPaperRequest request);

	@Operation(summary = "포장지 전체 조회", description = "포장지 페이지 조회합니다.")
	ResponseEntity<Page<GetWrappingPaperResponse>> getWrappingPaperAll(Pageable pageable);

	//List 전체조회
	@Operation(summary = "포장지 전체 조회", description = "포장지 모두 조회합니다.")
	ResponseEntity<GetWrappingPaperAllResponse> getWrappingPaperAllList();

	@Operation(summary = "포장지 단건 조회", description = "포장지 ID로 조회합니다.")
	ResponseEntity<GetWrappingPaperResponse> getWrappingPaper(@PathVariable Long id);

	@Operation(summary = "포장지 수정", description = "포장지 ID로 수정합니다.")
	ResponseEntity<Void> updateWrappingPaper(@PathVariable Long id,
		@Valid @RequestBody UpdateWrappingPaperRequest request);
}
