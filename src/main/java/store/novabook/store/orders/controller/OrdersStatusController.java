package store.novabook.store.orders.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import store.novabook.store.orders.controller.docs.OrdersStatusControllerDocs;
import store.novabook.store.orders.dto.request.CreateOrdersStatusRequest;
import store.novabook.store.orders.dto.response.CreateResponse;
import store.novabook.store.orders.dto.response.GetOrdersStatusResponse;
import store.novabook.store.orders.service.OrdersStatusService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/store/orders/status")
public class OrdersStatusController implements OrdersStatusControllerDocs {

	private final OrdersStatusService ordersStatusService;

	@PostMapping
	public ResponseEntity<CreateResponse> createOrdersStatus(@RequestBody CreateOrdersStatusRequest request) {
		CreateResponse response = ordersStatusService.save(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<GetOrdersStatusResponse> getOrdersStatus(@PathVariable Long id) {
		GetOrdersStatusResponse response = ordersStatusService.getOrdersStatus(id);
		return ResponseEntity.ok(response);
	}
}
