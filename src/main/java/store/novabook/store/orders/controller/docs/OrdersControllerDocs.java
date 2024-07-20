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
import store.novabook.store.orders.dto.request.UpdateOrdersAdminRequest;
import store.novabook.store.orders.dto.response.GetOrdersAdminResponse;
import store.novabook.store.orders.dto.response.GetOrdersResponse;

/**
 * 주문 관련 API 문서화 인터페이스입니다.
 * 이 인터페이스는 주문 조회, 수정 및 관리자용 주문 목록 조회를 문서화하는 데 사용됩니다.
 */
@Tag(name = "Orders API")
public interface OrdersControllerDocs {

	/**
	 * 주문 ID로 주문 정보를 조회합니다.
	 *
	 * @param id 주문 ID
	 * @return 주문 정보에 대한 응답
	 */
	@Operation(summary = "주문 조회", description = "주문 ID로 주문 정보를 조회합니다.")
	@Parameter(name = "id", description = "주문 ID", required = true)
	ResponseEntity<GetOrdersResponse> getOrders(@PathVariable Long id);

	/**
	 * 주문 ID로 주문 정보를 수정합니다.
	 *
	 * @param id 주문 ID
	 * @param request 수정할 주문 정보
	 * @return 수정 결과에 대한 응답
	 */
	@Operation(summary = "주문 수정", description = "주문 ID로 주문 정보를 수정합니다.")
	@Parameter(name = "id", description = "주문 ID", required = true)
	@Parameter(name = "request", description = "수정할 주문 정보", required = true)
	ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody UpdateOrdersAdminRequest request);

	/**
	 * 관리자 권한으로 모든 주문 정보를 페이지 단위로 조회합니다.
	 *
	 * @param pageable 페이지 정보
	 * @return 주문 목록의 페이지 응답
	 */
	@Operation(summary = "관리자 주문 목록 조회", description = "관리자 권한으로 모든 주문 정보를 페이지 단위로 조회합니다.")
	@Parameter(name = "pageable", description = "페이지 정보", required = true)
	ResponseEntity<Page<GetOrdersAdminResponse>> getOrdersAdmin(Pageable pageable);
}
