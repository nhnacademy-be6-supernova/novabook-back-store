package store.novabook.store.orders.controller.docs;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import store.novabook.store.orders.dto.request.CreateOrdersStatusRequest;
import store.novabook.store.orders.dto.response.CreateResponse;
import store.novabook.store.orders.dto.response.GetOrdersStatusResponse;

@Tag(name = "OrdersStatus API", description = "주문 상태 API")
public interface OrdersStatusControllerDocs {

	//생성
	@Operation(summary = "주문상태 생성", description = "주문상태 추가합니다.")
	ResponseEntity<CreateResponse> createOrdersStatus(@RequestBody CreateOrdersStatusRequest request);

	//단건 조회
	@Operation(summary = "주문상태 단건 조회", description = "주문상태 ID로 조회합니다.")
	ResponseEntity<GetOrdersStatusResponse> getOrdersStatus(@PathVariable Long id);
}
