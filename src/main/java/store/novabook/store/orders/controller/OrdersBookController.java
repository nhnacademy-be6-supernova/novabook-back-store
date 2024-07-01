package store.novabook.store.orders.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.store.orders.controller.docs.OrdersBookControllerDocs;

import store.novabook.store.orders.dto.request.CreateOrdersBookRequest;
import store.novabook.store.orders.dto.request.UpdateOrdersBookRequest;
import store.novabook.store.orders.dto.response.CreateResponse;
import store.novabook.store.orders.dto.response.GetOrdersBookResponse;
import store.novabook.store.orders.service.OrdersBookService;

@RestController
@RequestMapping("/api/v1/store/orders/book")
@RequiredArgsConstructor
public class OrdersBookController implements OrdersBookControllerDocs {
	private final OrdersBookService ordersBookService;

	@PostMapping
	public ResponseEntity<CreateResponse> createOrdersBook(@Valid @RequestBody CreateOrdersBookRequest request) {
		CreateResponse response = ordersBookService.create(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping
	public ResponseEntity<Page<GetOrdersBookResponse>> getOrdersBookAll() {
		Page<GetOrdersBookResponse> responses = ordersBookService.getOrdersBookAll();
		return ResponseEntity.ok(responses);
	}

	@GetMapping("/{id}")
	public ResponseEntity<GetOrdersBookResponse> getOrdersBook(@PathVariable Long id) {
		GetOrdersBookResponse response = ordersBookService.getOrdersBook(id);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> updateOrdersBook(@PathVariable Long id,
		@Valid @RequestBody UpdateOrdersBookRequest request) {
		ordersBookService.update(id, request);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteOrdersBook(@PathVariable Long id) {
		ordersBookService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
