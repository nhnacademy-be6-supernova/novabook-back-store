package store.novabook.store.payment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.store.payment.controller.docs.PaymentControllerDocs;
import store.novabook.store.payment.dto.request.CreatePaymentRequest;
import store.novabook.store.payment.dto.response.CreatePaymentResponse;
import store.novabook.store.payment.dto.response.GetPaymentResponse;
import store.novabook.store.payment.service.PaymentService;

@RestController
@RequestMapping("/api/v1/store/payments")
@RequiredArgsConstructor
public class PaymentController implements PaymentControllerDocs {

	private final PaymentService paymentService;

	@PostMapping
	public ResponseEntity<CreatePaymentResponse> createPayment(@Valid @RequestBody CreatePaymentRequest request) {
		CreatePaymentResponse response = paymentService.createPayment(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/{paymentId}")
	public ResponseEntity<GetPaymentResponse> getPayment(@PathVariable Long paymentId) {
		GetPaymentResponse response = paymentService.getPayment(paymentId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/orders/{ordersId}")
	public ResponseEntity<GetPaymentResponse> getPaymentByOrderId(@PathVariable Long ordersId) {
		GetPaymentResponse response = paymentService.getPaymentByOrderId(ordersId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
