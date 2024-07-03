package store.novabook.store.common.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import store.novabook.store.common.messaging.dto.CreateCouponMessage;

@Service
@RequiredArgsConstructor
public class CouponSender {

	private final RabbitTemplate rabbitTemplate;

	@Value("${rabbitmq.exchange.couponOperation}")
	private String couponOperationExchange;

	// welcome 쿠폰
	@Value("${rabbitmq.routing.couponCreateHighTraffic}")
	private String couponCreateHighTrafficRoutingKey;

	public void sendToHighTrafficQueue(CreateCouponMessage message) {
		rabbitTemplate.convertAndSend(couponOperationExchange, couponCreateHighTrafficRoutingKey, message);
	}


}
