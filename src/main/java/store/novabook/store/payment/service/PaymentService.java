package store.novabook.store.payment.service;

import org.springframework.transaction.annotation.Transactional;

import store.novabook.store.payment.dto.CreatePaymentRequest;
import store.novabook.store.payment.dto.CreatePaymentResponse;
import store.novabook.store.payment.dto.GetPaymentResponse;

public interface PaymentService {
	@Transactional(readOnly = true)
	GetPaymentResponse getPayment(Long paymentId);

	CreatePaymentResponse createPayment(CreatePaymentRequest request);

	@Transactional(readOnly = true)
	GetPaymentResponse getPaymentByOrderId(Long ordersId);
}
