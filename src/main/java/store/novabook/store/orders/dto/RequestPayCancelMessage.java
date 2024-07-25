package store.novabook.store.orders.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class RequestPayCancelMessage {
	PaymentType paymentType;
	long totalAmount;
	String orderCode;
	Long couponId;
	Long usePointAmount;
	Long memberId;
	Long earnPointAmount;
	String paymentKey;
	String status;
}
