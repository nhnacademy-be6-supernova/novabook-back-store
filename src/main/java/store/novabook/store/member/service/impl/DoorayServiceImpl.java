package store.novabook.store.member.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import store.novabook.store.member.dto.request.GetMembersUUIDRequest;
import store.novabook.store.member.dto.response.GetMembersUUIDResponse;
import store.novabook.store.member.service.AuthMembersClient;
import store.novabook.store.member.service.DoorayHookClient;
import store.novabook.store.member.service.DoorayService;

@Service
@RequiredArgsConstructor
public class DoorayServiceImpl implements DoorayService {

	private final DoorayHookClient doorayHookClient;
	private final AuthMembersClient authMembersClient;

	@Override
	public void sendAuthCode(String uuid, String authCode) {

		GetMembersUUIDRequest getMembersUUIDRequest = new GetMembersUUIDRequest(uuid);

		GetMembersUUIDResponse getMembersUUIDResponse = authMembersClient.getDormantMembersId(getMembersUUIDRequest);
		getMembersUUIDResponse.membersId();

		Map<String, Object> request = new HashMap<>();
		request.put("botName", "novabook Bot");
		request.put("text", "휴면 계정 해지를 위한 인증코드: " + authCode);

		doorayHookClient.sendMessage(request);
	}
}
