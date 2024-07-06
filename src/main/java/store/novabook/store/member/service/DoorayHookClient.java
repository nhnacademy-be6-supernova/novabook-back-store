package store.novabook.store.member.service;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "doorayHookClient", url = "https://hook.dooray.com/services/3204376758577275363/3841573584705463142/XgrMG9YtRw65XfNFuTYFDg")
public interface DoorayHookClient {

	@PostMapping
	void sendMessage(@RequestBody Map<String, Object> message);
}
