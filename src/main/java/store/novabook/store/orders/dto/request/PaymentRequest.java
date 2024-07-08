package store.novabook.store.orders.dto.request;

import java.util.UUID;

import store.novabook.store.orders.dto.PaymentType;
public record PaymentRequest(
	PaymentType type,
	UUID orderUUID,
	Object paymentInfo
) {}
