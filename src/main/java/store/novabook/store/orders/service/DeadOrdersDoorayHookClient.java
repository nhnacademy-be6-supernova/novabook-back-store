package store.novabook.store.orders.service;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "deadOrdersQueueDoorayHookClient")
public interface DeadOrdersDoorayHookClient {
	@PostMapping
	void sendMessage(@RequestBody Map<String, Object> message);
}
