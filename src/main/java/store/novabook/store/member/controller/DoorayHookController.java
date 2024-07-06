package store.novabook.store.member.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import store.novabook.store.common.security.aop.CurrentMembers;
import store.novabook.store.member.service.DoorayService;
import store.novabook.store.member.service.MemberService;

@RestController
@RequestMapping("/api/v1/store/dooray")
@RequiredArgsConstructor
public class DoorayHookController {
	private final MemberService memberService;
	private final DoorayService doorayService;

	@PostMapping("/sendAuthCode")
	public ResponseEntity<String> sendAuthCode(@CurrentMembers Long memberId) {
		if (!memberService.isDormantMember(memberId)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 활성화된 계정입니다.");
		}
		String authCode = memberService.createAndSaveAuthCode(memberId);
		doorayService.sendAuthCode(memberId, authCode);

		return ResponseEntity.ok("인증번호가 발송되었습니다.");
	}

	@PostMapping("/confirm")
	public ResponseEntity<String> confirmDormantMember(@RequestBody Map<String, String> request,
		@CurrentMembers Long memberId) {
		String inputCode = request.get("authCode");

		if (memberService.validateAuthCode(memberId, inputCode)) {
			memberService.deleteAuthCodeFromRedis(memberId);
			memberService.updateMemberStatusToActive(memberId, inputCode);
			return ResponseEntity.ok("해지가 성공적으로 완료되었습니다.");
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 코드입니다.");
		}
	}
}
