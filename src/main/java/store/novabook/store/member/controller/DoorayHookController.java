package store.novabook.store.member.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import store.novabook.store.member.service.DoorayService;
import store.novabook.store.member.service.MemberService;

@RestController
@RequestMapping("/dooray")
@RequiredArgsConstructor
public class DoorayHookController {
	private final MemberService memberService;
	private final DoorayService doorayService;

	// @PostMapping("/sendAuthCode")
	// public ResponseEntity<String> sendAuthCode(@CurrentUser Long memberId) {
	// }
}
