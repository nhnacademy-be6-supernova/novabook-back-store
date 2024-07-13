package store.novabook.store.orders.controller.docs;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import store.novabook.store.orders.dto.request.CreateOrdersRequest;
import store.novabook.store.orders.dto.request.UpdateOrdersAdminRequest;
import store.novabook.store.orders.dto.response.CreateResponse;
import store.novabook.store.orders.dto.response.GetOrdersResponse;

@Tag(name = "Orders API")
public interface OrdersControllerDocs {

	//전체 조회
	@Operation(summary = "주문 전체 조회", description = "주문을 전체 조회합니다.")
	ResponseEntity<Page<GetOrdersResponse>> getOrdersAll();

	//단건조회
	@Operation(summary = "주문 조회", description = "해당 ID로 주문을 조회합니다.")
	ResponseEntity<GetOrdersResponse> getOrders(@PathVariable Long id);

	//수정
	@Operation(summary = "주문 수정", description = "해당 ID의 주문을 수정합니다.")
	ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody UpdateOrdersAdminRequest request);
}
