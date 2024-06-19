package store.novabook.store.user.member.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.store.user.member.dto.CreateMemberRequest;
import store.novabook.store.user.member.dto.GetMemberResponse;
import store.novabook.store.user.member.entity.Member;
import store.novabook.store.user.member.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

	private final MemberService memberService;

	@PostMapping
	public ResponseEntity<Void> createMember(@Valid @RequestBody CreateMemberRequest createMemberRequest) {
		Member createdMember = memberService.createMember(createMemberRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(null);
	}

	@GetMapping
	public ResponseEntity<List<GetMemberResponse>> getMemberAll() {
		List<GetMemberResponse> memberAll = memberService.getMemberAll();
		return ResponseEntity.ok(memberAll);
	}

	@GetMapping("/{memberId}")
	public ResponseEntity<GetMemberResponse> getMember(@PathVariable Long memberId) {
		GetMemberResponse memberResponse = memberService.getMember(memberId);
		return ResponseEntity.ok(memberResponse);
	}

	@PutMapping("/{memberId}")
	public ResponseEntity<Void> updateMember(@PathVariable Long memberId,
		@Valid @RequestBody CreateMemberRequest createMemberRequest) {
		memberService.updateMember(memberId, createMemberRequest);
		return ResponseEntity.ok().body(null);
	}

	@DeleteMapping("/{memberId}")
	public ResponseEntity<Void> deleteMember(@PathVariable Long memberId) {
		memberService.deleteMember(memberId);
		return ResponseEntity.ok().body(null);
	}

}
