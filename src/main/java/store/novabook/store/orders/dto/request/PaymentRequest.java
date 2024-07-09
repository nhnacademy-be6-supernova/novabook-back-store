package store.novabook.store.orders.dto.request;

import java.io.Serializable;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import store.novabook.store.orders.dto.PaymentType;
public record PaymentRequest(
	PaymentType type,
	Long memberId,
	@NotNull
	UUID orderId,
	Object paymentInfo
) implements Serializable {}
