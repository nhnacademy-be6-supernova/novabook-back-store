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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.store.orders.dto.CreateOrdersRequest;
import store.novabook.store.orders.dto.CreateResponse;
import store.novabook.store.orders.dto.GetOrdersResponse;
import store.novabook.store.orders.dto.UpdateOrdersRequest;
import store.novabook.store.orders.service.OrdersService;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrdersController {
	private final OrdersService ordersService;

	//생성
	@PostMapping
	public ResponseEntity<CreateResponse> createOrders(@Valid @RequestBody CreateOrdersRequest request){
		CreateResponse response = ordersService.create(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	//전체 조회
	@GetMapping
	public ResponseEntity<Page<GetOrdersResponse>> getOrdersAll(){
		Page<GetOrdersResponse> responses = ordersService.getOrdersResponsesAll();
		return ResponseEntity.ok(responses);
	}

	//단건조회
	@GetMapping("/{id}")
	public ResponseEntity<GetOrdersResponse> getOrders(@PathVariable Long id){
		GetOrdersResponse response = ordersService.getOrdersById(id);
		return ResponseEntity.ok(response);
	}

	//수정
	@PutMapping("/{id}")
	public ResponseEntity<Void> update(@PathVariable Long id,@Valid @RequestBody UpdateOrdersRequest request){
		ordersService.update(id, request);
		return ResponseEntity.noContent().build();
	}

}
