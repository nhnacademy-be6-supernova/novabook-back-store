package store.novabook.store.orders.dto.request;

import store.novabook.store.orders.dto.PaymentType;
public record PaymentRequest(
	PaymentType type,
	Object paymentInfo
) {}
