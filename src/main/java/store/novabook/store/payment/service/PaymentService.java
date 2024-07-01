package store.novabook.store.payment.service;

import org.springframework.transaction.annotation.Transactional;

import store.novabook.store.payment.dto.request.CreatePaymentRequest;
import store.novabook.store.payment.dto.response.CreatePaymentResponse;
import store.novabook.store.payment.dto.response.GetPaymentResponse;

public interface PaymentService {
	@Transactional(readOnly = true)
	GetPaymentResponse getPayment(Long paymentId);

	CreatePaymentResponse createPayment(CreatePaymentRequest request);

	@Transactional(readOnly = true)
	GetPaymentResponse getPaymentByOrderId(Long ordersId);
}
