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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.store.orders.dto.CreateOrdersBookRequest;
import store.novabook.store.orders.dto.CreateResponse;
import store.novabook.store.orders.dto.GetOrdersBookResponse;
import store.novabook.store.orders.dto.UpdateOrdersBookRequest;
import store.novabook.store.orders.service.OrdersBookService;

@Tag(name = "OrdersBook API", description = "OrdersBook 을 생성 조회 수정, 삭제 합니다.")
@RestController
@RequestMapping("/api/v1/store/orders/book")
@RequiredArgsConstructor
public class OrdersBookController {
	private final OrdersBookService ordersBookService;

	//생성
	@Operation(summary = "생성", description = "생성합니다 ")
	@PostMapping
	public ResponseEntity<CreateResponse> createOrdersBook(@Valid @RequestBody CreateOrdersBookRequest request) {
		CreateResponse response = ordersBookService.create(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	//전체 조회
	@Operation(summary = "전체 조회", description = "전체 조회합니다.")
	@GetMapping
	public ResponseEntity<Page<GetOrdersBookResponse>> getOrdersBookAll() {
		Page<GetOrdersBookResponse> responses = ordersBookService.getOrdersBookAll();
		return ResponseEntity.ok(responses);
	}

	//단건조회
	@Operation(summary = "조회", description = "조회합니다.")
	@GetMapping("/{id}")
	public ResponseEntity<GetOrdersBookResponse> getOrdersBook(@PathVariable Long id) {
		GetOrdersBookResponse response = ordersBookService.getOrdersBook(id);
		return ResponseEntity.ok(response);
	}

	//수정
	@Operation(summary = "수정", description = "수정합니다.")
	@PutMapping("/{id}")
	public ResponseEntity<Void> updateOrdersBook(@PathVariable Long id,
		@Valid @RequestBody UpdateOrdersBookRequest request) {
		ordersBookService.update(id, request);
		return ResponseEntity.noContent().build();
	}

	//삭제
	@Operation(summary = "삭제", description = "삭제 합니다.")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteOrdersBook(@PathVariable Long id) {
		ordersBookService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
