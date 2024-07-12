package store.novabook.store.orders.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.store.orders.controller.docs.OrdersControllerDocs;
import store.novabook.store.orders.dto.request.CreateOrdersRequest;
import store.novabook.store.orders.dto.request.UpdateOrdersAdminRequest;
import store.novabook.store.orders.dto.response.CreateResponse;
import store.novabook.store.orders.dto.response.GetOrdersAdminResponse;
import store.novabook.store.orders.dto.response.GetOrdersResponse;
import store.novabook.store.orders.service.OrdersService;

@RestController
@RequestMapping("/api/v1/store/orders")
@RequiredArgsConstructor
public class OrdersController implements OrdersControllerDocs {

	private final OrdersService ordersService;

	@PostMapping
	public ResponseEntity<CreateResponse> createOrders(@Valid @RequestBody CreateOrdersRequest request) {
		CreateResponse response = ordersService.create(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping
	public ResponseEntity<Page<GetOrdersResponse>> getOrdersAll() {
		Page<GetOrdersResponse> responses = ordersService.getOrdersResponsesAll();
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/{id}")
	public ResponseEntity<GetOrdersResponse> getOrders(@PathVariable Long id) {
		GetOrdersResponse response = ordersService.getOrdersById(id);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody UpdateOrdersAdminRequest request) {
		ordersService.update(id, request);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/admin")
	public ResponseEntity<Page<GetOrdersAdminResponse>> getOrdersAdmin(Pageable pageable) {
		Page<GetOrdersAdminResponse> responses = ordersService.getOrdersAdminResponsesAll(pageable);
		return ResponseEntity.ok().body(responses);
	}

}
