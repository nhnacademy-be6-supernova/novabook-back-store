package store.novabook.store.payment.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.common.exception.NotFoundException;
import store.novabook.store.payment.dto.request.CreatePaymentRequest;
import store.novabook.store.payment.dto.response.CreatePaymentResponse;
import store.novabook.store.payment.dto.response.GetPaymentResponse;
import store.novabook.store.payment.entity.Payment;
import store.novabook.store.payment.repository.PaymentRepository;
import store.novabook.store.payment.service.PaymentService;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

	private final PaymentRepository paymentRepository;

	@Override
	@Transactional(readOnly = true)
	public GetPaymentResponse getPayment(Long paymentId) {
		Payment payment = paymentRepository.findById(paymentId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.PAYMENT_NOT_FOUND));
		return GetPaymentResponse.from(payment);
	}

	@Override
	public CreatePaymentResponse createPayment(CreatePaymentRequest request) {
		Payment payment = new Payment(request);
		paymentRepository.save(payment);
		return new CreatePaymentResponse(payment.getId());
	}
	//
	// @Override
	// @Transactional(readOnly = true)
	// public GetPaymentResponse getPaymentByOrderId(Long ordersId) {
	// 	Payment payment = paymentRepository.findByOrdersId(ordersId);
	// 	if (payment == null) {
	// 		throw new NotFoundException(ErrorCode.PAYMENT_NOT_FOUND);
	// 	}
	// 	return GetPaymentResponse.from(payment);
	// }
}
