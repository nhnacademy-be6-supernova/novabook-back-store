package store.novabook.store.orders.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class RequestPayCancelMessage {
	Long couponId;
	Long usePointAmount;
	Long memberId;
	Long earnPointAmoun;
	String paymentKey;
	String status;
}
