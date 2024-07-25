package store.novabook.store.common.util;

import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import store.novabook.store.orders.dto.PaymentType;
import store.novabook.store.payment.Payment;
import store.novabook.store.payment.TossPayment;

@Component
@RequiredArgsConstructor
public class PaymentFactory {
	private final Map<PaymentType, Payment> paymentStrategy = Map.of(PaymentType.TOSS, new TossPayment());
	public Payment getPaymentStrategy(PaymentType paymentType) {
		return paymentStrategy.get(paymentType);
	}
}
