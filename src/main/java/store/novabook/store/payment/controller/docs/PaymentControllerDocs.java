package store.novabook.store.payment.controller.docs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import store.novabook.store.payment.dto.request.CreatePaymentRequest;
import store.novabook.store.payment.dto.response.CreatePaymentResponse;
import store.novabook.store.payment.dto.response.GetPaymentResponse;

@Tag(name = "Payment API")
public interface PaymentControllerDocs {
	ResponseEntity<CreatePaymentResponse> createPayment(@Valid @RequestBody CreatePaymentRequest request);
	//결제 번호로 조회
	ResponseEntity<GetPaymentResponse> getPayment(@PathVariable Long paymentId);
	//주문 번호로 조회
	ResponseEntity<GetPaymentResponse> getPaymentByOrderId(@PathVariable Long ordersId);
}
