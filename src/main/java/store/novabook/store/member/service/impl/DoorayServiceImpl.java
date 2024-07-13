package store.novabook.store.member.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import store.novabook.store.member.dto.request.GetMembersUUIDRequest;
import store.novabook.store.member.dto.response.GetMembersUUIDResponse;
import store.novabook.store.member.service.AuthMembersClient;
import store.novabook.store.member.service.DoorayHookClient;
import store.novabook.store.member.service.DoorayService;
import store.novabook.store.orders.dto.OrderSagaMessage;
import store.novabook.store.orders.service.DeadOrdersDoorayHookClient;

@Service
@RequiredArgsConstructor
public class DoorayServiceImpl implements DoorayService {

	private final DoorayHookClient doorayHookClient;
	private final DeadOrdersDoorayHookClient deadOrdersDoorayHookClient;


	@Override
	public void sendAuthCode(String uuid, String authCode) {

		Map<String, Object> request = new HashMap<>();
		request.put("botName", "novabook Bot");
		request.put("text", "휴면 계정 해지를 위한 인증코드: " + authCode);

		doorayHookClient.sendMessage(request);
	}

	@RabbitListener(queues = "nova.orders.saga.dead.queue")
	public void sendDeadMessage(@Payload OrderSagaMessage orderSagaMessage) {
		Map<String, Object> request = new HashMap<>();
		request.put("botName", "novabook Bot");
		request.put("text", "주문 처리중 문제가 발생했습니다 \n상태:  " + orderSagaMessage.getStatus());
		deadOrdersDoorayHookClient.sendMessage(request);
	}

	@RabbitListener(queues = "nova.coupon.deadletter.queue")
	public void sendDeadMessage(@Header("x-original-queue") String originQueue) {
		Map<String, Object> request = new HashMap<>();
		request.put("botName", "novabook Bot");
		request.put("text", "쿠폰 처리중 문제가 발생했습니다 \n해당 QUEUE:  " + originQueue);
		deadOrdersDoorayHookClient.sendMessage(request);
	}


}
