package store.novabook.store.orders.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import store.novabook.store.orders.controller.docs.OrdersSagaControllerDocs;
import store.novabook.store.orders.dto.request.PaymentRequest;
import store.novabook.store.orders.service.impl.OrdersSagaManagerImpl;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/store/orders/saga")
public class OrdersSagaController implements OrdersSagaControllerDocs {

	private final OrdersSagaManagerImpl ordersSagaManager;

	@PostMapping
	public ResponseEntity<Void> createOrder(@RequestBody PaymentRequest paymentRequest) {
		ordersSagaManager.orderInvoke(paymentRequest);
		return ResponseEntity.ok().build();
	}
}
