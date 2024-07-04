package store.novabook.store.common.messaging;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponNotifier {

	private final RedisTemplate<String, String> redisTemplate;
	private final ChannelTopic topic;

	public void notify(String clientId, String message) {
		String notificationMessage = clientId + ":" + message;
		redisTemplate.convertAndSend(topic.getTopic(), notificationMessage);
	}
}
