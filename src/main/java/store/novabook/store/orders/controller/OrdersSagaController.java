package store.novabook.store.orders.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import store.novabook.store.orders.dto.request.PaymentRequest;
import store.novabook.store.orders.service.impl.OrdersSagaManagerImpl;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/store/orders/saga")
public class OrdersSagaController {

	private final OrdersSagaManagerImpl ordersSagaManager;

	/**
	 * 주문 트랜잭션을 시작하는 Manager
	 *
	 * @param paymentRequest 결제 시스템에서 넘어온 데이터
	 */
	@PostMapping
	public ResponseEntity<Void> createOrder(@RequestBody PaymentRequest paymentRequest) {
		ordersSagaManager.orderInvoke(paymentRequest);
		return ResponseEntity.ok().build();
	}
}
