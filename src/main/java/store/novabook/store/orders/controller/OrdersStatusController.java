package store.novabook.store.orders.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import store.novabook.store.orders.dto.CreateOrdersStatusRequest;
import store.novabook.store.orders.dto.CreateResponse;
import store.novabook.store.orders.dto.GetOrdersStatusResponse;
import store.novabook.store.orders.dto.UpdateOrdersStatusRequest;
import store.novabook.store.orders.service.OrdersStatusService;

@Tag(name = "Orders Status API", description = "Orders Status 을 생성 조회 수정합니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/store/orders/Status")
public class OrdersStatusController {
	private final OrdersStatusService ordersStatusService;

	//생성
	@Operation(summary = "생성", description = "생성합니다 ")
	@PostMapping
	public ResponseEntity<CreateResponse> createOrdersStatus(@RequestBody CreateOrdersStatusRequest request) {
		CreateResponse response = ordersStatusService.save(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	//전체 조회
	@Operation(summary = "전체 조회", description = "전체 조회합니다.")
	@GetMapping
	public ResponseEntity<Page<GetOrdersStatusResponse>> getOrdersStatusAll() {
		Page<GetOrdersStatusResponse> response = ordersStatusService.getOrdersStatus();
		return ResponseEntity.ok(response);
	}

	//단건 조회
	@Operation(summary = "조회", description = "조회합니다.")
	@GetMapping("/{id}")
	public ResponseEntity<GetOrdersStatusResponse> getOrdersStatus(@PathVariable Long id) {
		GetOrdersStatusResponse response = ordersStatusService.getOrdersStatus(id);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "수정", description = "수정합니다.")
	@PutMapping("/{id}")
	public ResponseEntity<Void> updateOrdersStatus(@PathVariable Long id,
		@RequestBody UpdateOrdersStatusRequest request) {
		ordersStatusService.updateOrdersStatus(id, request);
		return ResponseEntity.noContent().build();
	}
}
