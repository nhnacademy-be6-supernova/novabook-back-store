package store.novabook.store.payment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.exception.EntityNotFoundException;
import store.novabook.store.payment.dto.CreatePaymentRequest;
import store.novabook.store.payment.dto.CreatePaymentResponse;
import store.novabook.store.payment.dto.GetPaymentResponse;
import store.novabook.store.payment.entity.Payment;
import store.novabook.store.payment.repository.PaymentRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {
	private final PaymentRepository paymentRepository;

	//조회
	@Transactional(readOnly = true)
	public GetPaymentResponse getPayment(Long paymentId) {
		Payment payment = paymentRepository.findById(paymentId)
			.orElseThrow(() -> new EntityNotFoundException(Payment.class, paymentId));
		return GetPaymentResponse.from(payment);
	}

	//생성
	public CreatePaymentResponse createPayment(CreatePaymentRequest request) {
		Payment payment = new Payment(request);
		paymentRepository.save(payment);
		return new CreatePaymentResponse(payment.getId());
	}

	//주문 번호로 조회
	@Transactional(readOnly = true)
	public GetPaymentResponse getPaymentByOrderId(Long ordersId) {
		Payment payment = paymentRepository.findByOrdersId(ordersId);
		if (payment == null) {
			throw new EntityNotFoundException(Payment.class);
		}
		return GetPaymentResponse.from(payment);
	}
}
