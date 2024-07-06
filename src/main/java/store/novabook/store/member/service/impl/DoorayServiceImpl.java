package store.novabook.store.member.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import store.novabook.store.member.service.DoorayService;

@Service
public class DoorayServiceImpl implements DoorayService {

	private final RestTemplate restTemplate = new RestTemplate();
	private final String DOORAY_WEBHOOK_URL = "https://hook.dooray.com/services/3204376758577275363/3841573584705463142/XgrMG9YtRw65XfNFuTYFDg";

	@Override
	public void sendAuthCode(Long memberId, String authCode) {
		Map<String, Object> message = new HashMap<>();
		message.put("botName", memberId);
		message.put("text", "휴면 계정 해지를 위한 인증코드" + authCode);

		restTemplate.postForObject(DOORAY_WEBHOOK_URL, message, String.class);
	}
}
