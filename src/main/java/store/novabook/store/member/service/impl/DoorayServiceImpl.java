package store.novabook.store.member.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import store.novabook.store.member.service.DoorayHookClient;
import store.novabook.store.member.service.DoorayService;

@Service
@RequiredArgsConstructor
public class DoorayServiceImpl implements DoorayService {

	private final DoorayHookClient doorayHookClient;

	@Override
	public void sendAuthCode(Long memberId, String authCode) {
		Map<String, Object> message = new HashMap<>();
		message.put("botName", "novabook Bot");
		message.put("text", "휴면 계정 해지를 위한 인증코드: " + authCode);

		doorayHookClient.sendMessage(message);
	}
}
