package store.novabook.store.orders.controller.docs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import store.novabook.store.orders.dto.request.CreateOrdersStatusRequest;
import store.novabook.store.orders.dto.response.CreateResponse;
import store.novabook.store.orders.dto.response.GetOrdersStatusResponse;

/**
 * 주문 상태 API 문서화 인터페이스입니다.
 * 이 인터페이스는 주문 상태의 생성 및 조회 기능을 문서화합니다.
 */
@Tag(name = "OrdersStatus API", description = "주문 상태 API")
public interface OrdersStatusControllerDocs {

	/**
	 * 새로운 주문 상태를 생성합니다.
	 *
	 * @param request 생성할 주문 상태 정보
	 * @return 생성된 주문 상태에 대한 응답
	 */
	@Operation(summary = "주문 상태 생성", description = "주문 상태를 생성합니다.")
	@Parameter(name = "request", description = "생성할 주문 상태 정보", required = true)
	ResponseEntity<CreateResponse> createOrdersStatus(@RequestBody CreateOrdersStatusRequest request);

	/**
	 * 주문 상태 ID로 주문 상태 정보를 조회합니다.
	 *
	 * @param id 주문 상태 ID
	 * @return 주문 상태에 대한 응답
	 */
	@Operation(summary = "주문 상태 단건 조회", description = "주문 상태 ID로 주문 상태 정보를 조회합니다.")
	@Parameter(name = "id", description = "주문 상태 ID", required = true)
	ResponseEntity<GetOrdersStatusResponse> getOrdersStatus(@PathVariable Long id);
}
