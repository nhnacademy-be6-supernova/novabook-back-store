package store.novabook.store.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import store.novabook.store.member.service.DoorayService;
import store.novabook.store.member.service.MemberService;

@RestController
@RequestMapping("/api/v1/store/dooray")
@RequiredArgsConstructor
public class DoorayHookController {
	public static final long MEMBER_ID = 32L;
	private final MemberService memberService;
	private final DoorayService doorayService;

	@PostMapping("/sendAuthCode")
	public ResponseEntity<String> sendAuthCode(@RequestBody DoorayAuthRequest request) {
		if (!memberService.isDormantMember(MEMBER_ID)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 활성화된 계정입니다.");
		}
		String authCode = memberService.createAndSaveAuthCode(request.uuid());
		doorayService.sendAuthCode(MEMBER_ID, authCode);

		return ResponseEntity.ok("인증번호가 발송되었습니다.");
	}

	@PostMapping("/confirm")
	public ResponseEntity<String> confirmDormantMember(
		@RequestBody DoorayAuthCodeRequest request) {
		memberService.updateMemberStatusToActive(MEMBER_ID, request);
		return ResponseEntity.ok("해지가 성공적으로 완료되었습니다.");
	}
}
