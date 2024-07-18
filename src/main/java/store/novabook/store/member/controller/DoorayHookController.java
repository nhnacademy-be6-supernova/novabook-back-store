package store.novabook.store.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import store.novabook.store.member.dto.request.DoorayAuthCodeRequest;
import store.novabook.store.member.dto.request.DoorayAuthRequest;
import store.novabook.store.member.service.DoorayService;
import store.novabook.store.member.service.MemberService;

@RestController
@RequestMapping("/api/v1/store/dooray")
@RequiredArgsConstructor
public class DoorayHookController {
	private final MemberService memberService;
	private final DoorayService doorayService;

	@PostMapping("/send-auth-code")
	public ResponseEntity<Void> sendMessage(@RequestBody DoorayAuthRequest request) {
		String authCode = memberService.createAndSaveAuthCode(request.uuid());
		doorayService.sendAuthCode(request.uuid(), authCode);

		return ResponseEntity.ok().build();
	}

	@PostMapping("/confirm")
	public ResponseEntity<Void> confirmDormantMember(@RequestBody DoorayAuthCodeRequest request) {
		memberService.updateMemberStatusToActive(request);
		return ResponseEntity.ok().build();
	}
}
