package store.novabook.store.member.service;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "doorayHookClient")
public interface DoorayHookClient {

	@PostMapping
	void sendMessage(@RequestBody Map<String, Object> message);
}
