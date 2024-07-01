package store.novabook.store.orders.controller.docs;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import store.novabook.store.orders.dto.request.CreateDeliveryFeeRequest;
import store.novabook.store.orders.dto.response.CreateResponse;
import store.novabook.store.orders.dto.response.GetDeliveryFeeListResponse;
import store.novabook.store.orders.dto.response.GetDeliveryFeeResponse;

@Tag(name = "DeliveryFee API", description = "택배비 API")
public interface DeliveryFeeControllerDocs {

	//생성
	@Operation(summary = "택배비 금액 생성", description = "택배비를 추가합니다.")
	ResponseEntity<CreateResponse> createDeliveryFee(@Valid @RequestBody CreateDeliveryFeeRequest request);

	//전체 조회
	@Operation(summary = "택배비 조회", description = "택배비를 페이지 조회 합니다.")
	ResponseEntity<Page<GetDeliveryFeeResponse>> getDeliveryFeeAll(Pageable pageable);

	@Operation(summary = "택배비 전체 조회", description = "전체 조회 합니다.")
	ResponseEntity<GetDeliveryFeeListResponse> getDeliveryFeeAllList();

	//단건 조회
	@Operation(summary = "택배비 단건 조회", description = "택배비 ID로 조회 합니다.")
	ResponseEntity<GetDeliveryFeeResponse> getDeliveryFee(@PathVariable Long id);

}
