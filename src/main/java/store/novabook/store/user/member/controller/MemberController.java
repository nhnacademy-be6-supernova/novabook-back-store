package store.novabook.store.user.member.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import store.novabook.store.user.member.dto.FindMemberLoginResponse;
import store.novabook.store.user.member.dto.FindMemberRequest;
import store.novabook.store.user.member.dto.GetMemberResponse;
import store.novabook.store.user.member.dto.GetMembersUUIDRequest;
import store.novabook.store.user.member.dto.GetMembersUUIDResponse;
import store.novabook.store.user.member.dto.LoginMemberRequest;
import store.novabook.store.user.member.dto.LoginMemberResponse;
import store.novabook.store.user.member.dto.UpdateMemberRequest;
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

	@CrossOrigin(origins = "http://localhost:8080")
	@GetMapping("/checkDuplicatedLoginId")
	public ResponseEntity<Map<String, Boolean>> checkDuplicated(@RequestParam String loginId) {
		boolean isDuplicateLoginId = memberService.isDuplicateLoginId(loginId);
		Map<String, Boolean> response = new HashMap<>();
		response.put("isDuplicateLoginId", isDuplicateLoginId);
		return ResponseEntity.ok(response);
	}

	@CrossOrigin(origins = "http://localhost:8080")
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

	@GetMapping("/{memberId}")
	public ResponseEntity<GetMemberResponse> getMember(@PathVariable Long memberId) {
		GetMemberResponse memberResponse = memberService.getMember(memberId);
		return ResponseEntity.ok(memberResponse);
	}

	@PostMapping("/login")
	public ResponseEntity<LoginMemberResponse> login(@RequestBody LoginMemberRequest loginMemberRequest) {
		return ResponseEntity.ok().body(memberService.matches(loginMemberRequest));
	}

	@PutMapping("/{memberId}")
	public ResponseEntity<Void> updateMember(@PathVariable Long memberId,
		@RequestBody UpdateMemberRequest updateMemberRequest) {
		memberService.updateMember(memberId, updateMemberRequest);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/{memberId}/dormant")
	public ResponseEntity<Void> updateMemberStatusToDormant(@PathVariable Long memberId) {
		memberService.updateMemberStatusToDormant(memberId);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/{memberId}/withdrawn")
	public ResponseEntity<Void> updateMemberStatusToWithdrawn(@PathVariable Long memberId) {
		memberService.updateMemberStatusToWithdrawn(memberId);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/find")
	public ResponseEntity<FindMemberLoginResponse> find(@RequestBody FindMemberRequest findMemberRequest) {
		FindMemberLoginResponse memberLoginResponse = memberService.findMemberLogin(findMemberRequest.memberId());
		// return ResponseEntity.ok().body();
		return ResponseEntity.ok(memberLoginResponse);
	}

	@PostMapping("/find/admin")
	public ResponseEntity<FindMemberLoginResponse> findAdmin(@RequestBody FindMemberRequest findMemberRequest) {
		FindMemberLoginResponse memberLoginResponse = memberService.findMemberLogin(findMemberRequest.memberId());
		// return ResponseEntity.ok().body();
		return ResponseEntity.ok(memberLoginResponse);
	}

	@CheckRole("ROLE_USER")
	@PostMapping("/uuid")
	public ResponseEntity<GetMembersUUIDResponse> findUUID(@RequestHeader("Authorization") String authorization,
		@RequestBody GetMembersUUIDRequest getMembersUUIDRequest) {
		// Now you can use the "authorization" variable which contains the value of the "Authorization" header
		GetMembersUUIDResponse membersId = memberClient.getMembersId(getMembersUUIDRequest);

		return ResponseEntity.ok(membersId);
	}
}
