package store.novabook.store.orders.controller.docs;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import store.novabook.store.orders.dto.request.CreateDeliveryFeeRequest;
import store.novabook.store.orders.dto.response.CreateResponse;
import store.novabook.store.orders.dto.response.GetDeliveryFeeListResponse;
import store.novabook.store.orders.dto.response.GetDeliveryFeeResponse;

/**
 * 택배비 API 문서화 인터페이스입니다.
 * 이 인터페이스는 택배비 관련 API를 문서화하는 데 사용됩니다.
 */
@Tag(name = "DeliveryFee API", description = "택배비 API")
public interface DeliveryFeeControllerDocs {

	/**
	 * 새로운 택배비를 생성합니다.
	 *
	 * @param request 택배비 생성 요청 데이터
	 * @return 생성된 택배비에 대한 응답
	 */
	@Operation(summary = "택배비 금액 생성", description = "새로운 택배비를 생성합니다.")
	@Parameter(description = "택배비 생성 요청 데이터", required = true)
	ResponseEntity<CreateResponse> createDeliveryFee(@Valid @RequestBody CreateDeliveryFeeRequest request);

	/**
	 * 택배비를 페이지 단위로 조회합니다.
	 *
	 * @param pageable 페이지 정보
	 * @return 택배비 목록의 페이지 응답
	 */
	@Operation(summary = "택배비 조회", description = "택배비를 페이지 단위로 조회합니다.")
	@Parameter(description = "페이지 정보", required = true)
	ResponseEntity<Page<GetDeliveryFeeResponse>> getDeliveryAllPage(Pageable pageable);

	/**
	 * 모든 택배비를 조회합니다.
	 *
	 * @return 전체 택배비 목록에 대한 응답
	 */
	@Operation(summary = "택배비 전체 조회", description = "모든 택배비를 조회합니다.")
	ResponseEntity<GetDeliveryFeeListResponse> getDeliveryFeeAllList();

	/**
	 * 최근 생성된 택배비를 조회합니다.
	 *
	 * @return 최근 택배비에 대한 응답
	 */
	@Operation(summary = "최근 택배비 조회", description = "최근 생성된 택배비를 조회합니다.")
	ResponseEntity<GetDeliveryFeeResponse> getRecentDeliveryFee();

	/**
	 * ID로 특정 택배비를 조회합니다.
	 *
	 * @param id 택배비 ID
	 * @return 조회된 택배비에 대한 응답
	 */
	@Operation(summary = "택배비 단건 조회", description = "택배비 ID로 특정 택배비를 조회합니다.")
	@Parameter(description = "택배비 ID", required = true)
	ResponseEntity<GetDeliveryFeeResponse> getDeliveryFee(@PathVariable Long id);

}
