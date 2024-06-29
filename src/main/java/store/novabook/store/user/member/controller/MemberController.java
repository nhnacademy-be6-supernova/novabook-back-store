package store.novabook.store.user.member.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.store.common.security.aop.CheckRole;
import store.novabook.store.user.member.MemberClient;
import store.novabook.store.user.member.dto.CreateMemberRequest;
import store.novabook.store.user.member.dto.CreateMemberResponse;
import store.novabook.store.user.member.dto.DeleteMemberRequest;
import store.novabook.store.user.member.dto.FindMemberLoginResponse;
import store.novabook.store.user.member.dto.FindMemberRequest;
import store.novabook.store.user.member.dto.GetMemberResponse;
import store.novabook.store.user.member.dto.GetMembersUUIDRequest;
import store.novabook.store.user.member.dto.GetMembersUUIDResponse;
import store.novabook.store.user.member.dto.LoginMemberRequest;
import store.novabook.store.user.member.dto.LoginMemberResponse;
import store.novabook.store.user.member.dto.UpdateMemberNameRequest;
import store.novabook.store.user.member.dto.UpdateMemberNumberRequest;
import store.novabook.store.user.member.dto.UpdateMemberPasswordRequest;
import store.novabook.store.user.member.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/store/members")
public class MemberController {

	private final MemberService memberService;
	private final MemberClient memberClient;

	@PostMapping
	public ResponseEntity<CreateMemberResponse> createMember(
		@RequestBody @Valid CreateMemberRequest createMemberRequest) {
		CreateMemberResponse saved = memberService.createMember(createMemberRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}

	@GetMapping("/checkDuplicatedLoginId")
	public ResponseEntity<Map<String, Boolean>> checkDuplicated(@RequestParam String loginId) {
		boolean isDuplicateLoginId = memberService.isDuplicateLoginId(loginId);
		Map<String, Boolean> response = new HashMap<>();
		response.put("isDuplicateLoginId", isDuplicateLoginId);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/checkDuplicatedEmail")
	public ResponseEntity<Map<String, Boolean>> checkDuplicateEmail(@RequestBody Map<String, String> request) {
		String email = request.get("email");
		boolean isDuplicateEmail = memberService.isDuplicateEmail(email);
		Map<String, Boolean> response = new HashMap<>();
		response.put("isDuplicateEmail", isDuplicateEmail);
		return ResponseEntity.ok(response);
	}

	@GetMapping
	public ResponseEntity<Page<GetMemberResponse>> getMemberAll(Pageable pageable) {
		return ResponseEntity.ok(memberService.getMemberAll(pageable));
	}

	@GetMapping("/member")
	public ResponseEntity<GetMemberResponse> getMember(@RequestHeader Long memberId) {
		GetMemberResponse memberResponse = memberService.getMember(memberId);
		return ResponseEntity.ok(memberResponse);
	}

	@PostMapping("/login")
	public ResponseEntity<LoginMemberResponse> login(@RequestBody LoginMemberRequest loginMemberRequest) {
		return ResponseEntity.ok().body(memberService.matches(loginMemberRequest));
	}

	@PutMapping("/member/name")
	public ResponseEntity<Void> updateMemberName(@RequestHeader Long memberId,
		@RequestBody @Valid UpdateMemberNameRequest updateMemberNameRequest) {
		memberService.updateMemberName(memberId, updateMemberNameRequest);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/member/number")
	public ResponseEntity<Void> updateMemberNumber(@RequestHeader Long memberId,
		@RequestBody @Valid UpdateMemberNumberRequest updateMemberNumberRequest) {
		memberService.updateMemberNumber(memberId, updateMemberNumberRequest);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/member/password")
	public ResponseEntity<Void> updateMemberPassword(@RequestHeader Long memberId,
		@RequestBody @Valid UpdateMemberPasswordRequest updateMemberPasswordRequest) {
		memberService.updateMemberPassword(memberId, updateMemberPasswordRequest);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/member/dormant")
	public ResponseEntity<Void> updateMemberStatusToDormant(@RequestHeader Long memberId) {
		memberService.updateMemberStatusToDormant(memberId);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/member/withdraw")
	public ResponseEntity<Void> updateMemberStatusToWithdraw(@RequestHeader Long memberId,
		DeleteMemberRequest deleteMemberRequest) {
		memberService.updateMemberStatusToWithdraw(memberId, deleteMemberRequest);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/find")
	public ResponseEntity<FindMemberLoginResponse> find(@Valid @RequestBody FindMemberRequest findMemberRequest) {
		FindMemberLoginResponse memberLoginResponse = memberService.findMemberLogin(findMemberRequest.loginId());
		return ResponseEntity.ok(memberLoginResponse);
	}

	@PostMapping("/find/admin")
	public ResponseEntity<FindMemberLoginResponse> findAdmin(@Valid @RequestBody FindMemberRequest findMemberRequest) {
		FindMemberLoginResponse memberLoginResponse = memberService.findMemberLogin(findMemberRequest.loginId());
		return ResponseEntity.ok(memberLoginResponse);
	}

	@CheckRole("ROLE_USER")
	@PostMapping("/uuid")
	public ResponseEntity<GetMembersUUIDResponse> findUUID(@RequestHeader("Authorization") String authorization,
		@RequestBody GetMembersUUIDRequest getMembersUUIDRequest) {
		GetMembersUUIDResponse membersId = memberClient.getMembersId(getMembersUUIDRequest);

		return ResponseEntity.ok(membersId);
	}
}
