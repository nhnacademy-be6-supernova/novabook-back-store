package store.novabook.store.orders.dto.request;

import java.io.Serializable;
import java.util.UUID;

import store.novabook.store.orders.dto.PaymentType;
public record PaymentRequest(
	PaymentType type,
	UUID orderUUID,
	Object paymentInfo
) implements Serializable {}
