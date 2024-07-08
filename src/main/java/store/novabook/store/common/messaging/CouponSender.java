package store.novabook.store.common.messaging;

import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import store.novabook.store.common.messaging.dto.CreateCouponMessage;
import store.novabook.store.common.messaging.dto.CreateCouponNotifyMessage;

@Service
@RequiredArgsConstructor
public class CouponSender {

	private final RabbitTemplate rabbitTemplate;

	@Value("${rabbitmq.exchange.couponOperation}")
	private String couponOperationExchange;

	// welcome 쿠폰
	@Value("${rabbitmq.routing.couponCreateHighTraffic}")
	private String couponCreateHighTrafficRoutingKey;

	// welcome 쿠폰
	@Value("${rabbitmq.routing.couponCreateNormal}")
	private String couponCreateNormalRoutingKey;

	public void sendToNotifyQueue(String token, String refresh, CreateCouponNotifyMessage message) {
		rabbitTemplate.convertAndSend(couponOperationExchange, couponCreateHighTrafficRoutingKey, message,
			addHeaders(token, refresh));
	}

	//welcome - token 이 없음
	public void sendToNormalQueue(CreateCouponMessage message) {
		rabbitTemplate.convertAndSend(couponOperationExchange, couponCreateNormalRoutingKey, message);
	}

	private MessagePostProcessor addHeaders(String token, String refresh) {
		return message -> {
			message.getMessageProperties().setHeader("Authorization", token);
			message.getMessageProperties().setHeader("Refresh", refresh);
			return message;
		};
	}
}
