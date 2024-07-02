package store.novabook.store.member.controller;

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
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.store.common.security.aop.CheckRole;
import store.novabook.store.common.security.aop.CurrentUser;
import store.novabook.store.member.MemberClient;
import store.novabook.store.member.controller.docs.MemberControllerDocs;
import store.novabook.store.member.dto.request.CreateMemberRequest;
import store.novabook.store.member.dto.request.DeleteMemberRequest;
import store.novabook.store.member.dto.request.DuplicateEmailRequest;
import store.novabook.store.member.dto.request.DuplicateLoginIdRequest;
import store.novabook.store.member.dto.request.FindMemberRequest;
import store.novabook.store.member.dto.request.GetMembersUUIDRequest;
import store.novabook.store.member.dto.request.LoginMemberRequest;
import store.novabook.store.member.dto.request.UpdateMemberPasswordRequest;
import store.novabook.store.member.dto.request.UpdateMemberRequest;
import store.novabook.store.member.dto.response.CreateMemberResponse;
import store.novabook.store.member.dto.response.DuplicateResponse;
import store.novabook.store.member.dto.response.FindMemberLoginResponse;
import store.novabook.store.member.dto.response.GetMemberResponse;
import store.novabook.store.member.dto.response.GetMembersUUIDResponse;
import store.novabook.store.member.dto.response.LoginMemberResponse;
import store.novabook.store.member.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/store/members")
public class MemberController implements MemberControllerDocs {

	private final MemberService memberService;
	private final MemberClient memberClient;

	@PostMapping
	public ResponseEntity<CreateMemberResponse> createMember(
		@RequestBody @Valid CreateMemberRequest createMemberRequest) {
		CreateMemberResponse saved = memberService.createMember(createMemberRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}

	@PostMapping("/loginId/is-creatable")
	public ResponseEntity<DuplicateResponse> checkDuplicateLoginId(@RequestBody DuplicateLoginIdRequest request) {
		DuplicateResponse isDuplicateLoginId = memberService.isDuplicateLoginId(request.loginId());
		return ResponseEntity.ok().body(isDuplicateLoginId);
	}

	@PostMapping("/email/is-creatable")
	public ResponseEntity<DuplicateResponse> checkDuplicateEmail(@RequestBody DuplicateEmailRequest request) {
		DuplicateResponse isDuplicateEmail = memberService.isDuplicateEmail(request.email());
		return ResponseEntity.ok().body(isDuplicateEmail);
	}

	@GetMapping
	public ResponseEntity<Page<GetMemberResponse>> getMemberAll(Pageable pageable) {
		return ResponseEntity.ok(memberService.getMemberAll(pageable));
	}

	@GetMapping("/member")
	public ResponseEntity<GetMemberResponse> getMember(@CurrentUser Long memberId) {
		GetMemberResponse memberResponse = memberService.getMember(memberId);
		return ResponseEntity.ok(memberResponse);
	}

	@PostMapping("/login")
	public ResponseEntity<LoginMemberResponse> login(@RequestBody LoginMemberRequest loginMemberRequest) {
		return ResponseEntity.ok().body(memberService.matches(loginMemberRequest));
	}

	@PutMapping("/member/update")
	public ResponseEntity<Void> updateMember(@CurrentUser Long memberId,
		@RequestBody UpdateMemberRequest updateMemberRequest) {
		memberService.updateMemberNumberOrName(memberId, updateMemberRequest);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/member/password")
	public ResponseEntity<Void> updateMemberPassword(@CurrentUser Long memberId,
		@RequestBody @Valid UpdateMemberPasswordRequest updateMemberPasswordRequest) {
		memberService.updateMemberPassword(memberId, updateMemberPasswordRequest);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/member/dormant")
	public ResponseEntity<Void> updateMemberStatusToDormant(@CurrentUser Long memberId) {
		memberService.updateMemberStatusToDormant(memberId);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/member/withdraw")
	public ResponseEntity<Void> updateMemberStatusToWithdraw(@CurrentUser Long memberId,
		@RequestBody DeleteMemberRequest deleteMemberRequest) {
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
