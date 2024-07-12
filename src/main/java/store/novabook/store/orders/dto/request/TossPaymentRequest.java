package store.novabook.store.orders.dto.request;

public record TossPaymentRequest(
	String orderId,
	long amount,
	String paymentKey) {
}
