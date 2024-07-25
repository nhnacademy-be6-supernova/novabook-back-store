package store.novabook.store.orders.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import store.novabook.store.orders.dto.request.PaymentRequest;

@ToString
@Setter
@Getter
@Builder
@JsonDeserialize(builder = OrderSagaMessage.OrderSagaMessageBuilder.class)
public class OrderSagaMessage {
	long bookAmount;   //순수 금액
	long calculateTotalAmount;  //총 결제금액
	long couponAmount; //쿠폰 사용 금액
	long earnPointAmount; //적립금액
	Boolean isNoEarnPoint;
	Boolean isNoUsePoint;
	Boolean isNoUseCoupon;
	String status;
	PaymentRequest paymentRequest;

	@JsonPOJOBuilder(withPrefix = "")
	public static class OrderSagaMessageBuilder {
	}
}
