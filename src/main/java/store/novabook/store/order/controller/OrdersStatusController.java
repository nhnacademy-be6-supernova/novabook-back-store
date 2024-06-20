package store.novabook.store.order.controller;

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

import lombok.RequiredArgsConstructor;
import store.novabook.store.order.dto.CreateOrdersStatusRequest;
import store.novabook.store.order.dto.CreateResponse;
import store.novabook.store.order.dto.GetOrdersStatusResponse;
import store.novabook.store.order.dto.UpdateOrdersStatusRequest;
import store.novabook.store.order.entity.OrdersStatus;
import store.novabook.store.order.repository.OrdersStatusRepository;
import store.novabook.store.order.service.OrdersStatusService;

@RestController
@RequiredArgsConstructor
@RequestMapping("Orders/Status")
public class OrdersStatusController {
	private final OrdersStatusService ordersStatusService;

	//생성
	@PostMapping
	public ResponseEntity<CreateResponse> create(@RequestBody CreateOrdersStatusRequest request) {
		CreateResponse response = ordersStatusService.save(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	//전체 조회
	@GetMapping
	public ResponseEntity<Page<GetOrdersStatusResponse>> getOrdersStatus(){
		Page<GetOrdersStatusResponse> response = ordersStatusService.getOrdersStatus();
		return ResponseEntity.ok(response);
	}

	//단건 조회
	@GetMapping("/{id}")
	public ResponseEntity<GetOrdersStatusResponse> getOrdersStatus(@PathVariable Long id) {
		GetOrdersStatusResponse response = ordersStatusService.getOrdersStatus(id);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody UpdateOrdersStatusRequest request) {
		ordersStatusService.updateOrdersStatus(id,request);
		return ResponseEntity.noContent().build();
	}
}
