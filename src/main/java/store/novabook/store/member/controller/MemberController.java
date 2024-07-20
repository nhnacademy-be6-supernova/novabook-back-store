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
import store.novabook.store.common.security.aop.CurrentMembers;
import store.novabook.store.member.controller.docs.MemberControllerDocs;
import store.novabook.store.member.dto.request.CreateMemberRequest;
import store.novabook.store.member.dto.request.DeleteMemberRequest;
import store.novabook.store.member.dto.request.DuplicateEmailRequest;
import store.novabook.store.member.dto.request.DuplicateLoginIdRequest;
import store.novabook.store.member.dto.request.FindMemberRequest;
import store.novabook.store.member.dto.request.GetDormantMembersRequest;
import store.novabook.store.member.dto.request.GetMembersUUIDRequest;
import store.novabook.store.member.dto.request.GetPaycoMembersRequest;
import store.novabook.store.member.dto.request.LinkPaycoMembersRequest;
import store.novabook.store.member.dto.request.LoginMemberRequest;
import store.novabook.store.member.dto.request.UpdateMemberPasswordRequest;
import store.novabook.store.member.dto.request.UpdateMemberRequest;
import store.novabook.store.member.dto.response.CreateMemberResponse;
import store.novabook.store.member.dto.response.DuplicateResponse;
import store.novabook.store.member.dto.response.FindMemberLoginResponse;
import store.novabook.store.member.dto.response.GetDormantMembersResponse;
import store.novabook.store.member.dto.response.GetMemberResponse;
import store.novabook.store.member.dto.response.GetMembersUUIDResponse;
import store.novabook.store.member.dto.response.GetPaycoMembersResponse;
import store.novabook.store.member.dto.response.GetmemberNameResponse;
import store.novabook.store.member.dto.response.LoginMemberResponse;
import store.novabook.store.member.service.AuthMembersClient;
import store.novabook.store.member.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/store/members")
public class MemberController implements MemberControllerDocs {

	private final MemberService memberService;
	private final AuthMembersClient authMembersClient;

	@PostMapping
	public ResponseEntity<CreateMemberResponse> createMember(
		@RequestBody @Valid CreateMemberRequest createMemberRequest) {
		CreateMemberResponse saved = memberService.createMember(createMemberRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}

	@PostMapping("/login-id/is-duplicate")
	public ResponseEntity<DuplicateResponse> checkDuplicateLoginId(@RequestBody DuplicateLoginIdRequest request) {
		DuplicateResponse isDuplicateLoginId = memberService.isDuplicateLoginId(request.loginId());
		return ResponseEntity.ok().body(isDuplicateLoginId);
	}

	@PostMapping("/email/is-duplicate")
	public ResponseEntity<DuplicateResponse> checkDuplicateEmail(@RequestBody DuplicateEmailRequest request) {
		DuplicateResponse isDuplicateEmail = memberService.isDuplicateEmail(request.email());
		return ResponseEntity.ok().body(isDuplicateEmail);
	}

	@GetMapping
	public ResponseEntity<Page<GetMemberResponse>> getMemberAll(Pageable pageable) {
		return ResponseEntity.ok(memberService.getMemberAll(pageable));
	}

	@CheckRole({"ROLE_ADMIN", "ROLE_MEMBERS"})
	@GetMapping("/member")
	public ResponseEntity<GetMemberResponse> getMember(@CurrentMembers(required = false) Long memberId) {
		if (memberId != null) {
			GetMemberResponse memberResponse = memberService.getMember(memberId);
			return ResponseEntity.ok(memberResponse);
		}
		return ResponseEntity.ok().build();
	}

	@GetMapping("/member/name")
	public ResponseEntity<GetmemberNameResponse> getMemberName(@CurrentMembers(required = false) Long memberId) {
		if (memberId == null) {
			return ResponseEntity.ok(new GetmemberNameResponse("비회원"));
		}
		return ResponseEntity.ok().body(memberService.getMemberName(memberId));
	}

	@PostMapping("/login")
	public ResponseEntity<LoginMemberResponse> login(@RequestBody LoginMemberRequest loginMemberRequest) {
		return ResponseEntity.ok().body(memberService.matches(loginMemberRequest));
	}

	@CheckRole({"ROLE_ADMIN", "ROLE_MEMBERS"})
	@PutMapping("/member/update")
	public ResponseEntity<Void> updateMember(@CurrentMembers Long memberId,
		@RequestBody UpdateMemberRequest updateMemberRequest) {
		memberService.updateMemberNumberOrName(memberId, updateMemberRequest);

		return ResponseEntity.ok().build();
	}

	@CheckRole({"ROLE_ADMIN", "ROLE_MEMBERS"})
	@PutMapping("/member/password")
	public ResponseEntity<Void> updateMemberPassword(@CurrentMembers Long memberId,
		@RequestBody @Valid UpdateMemberPasswordRequest updateMemberPasswordRequest) {
		memberService.updateMemberPassword(memberId, updateMemberPasswordRequest);
		return ResponseEntity.ok().build();
	}

	@CheckRole({"ROLE_ADMIN", "ROLE_MEMBERS"})
	@PutMapping("/member/dormant")
	public ResponseEntity<Void> updateMemberStatusToDormant(@CurrentMembers Long memberId) {
		memberService.updateMemberStatusToDormant(memberId);
		return ResponseEntity.ok().build();
	}

	@CheckRole({"ROLE_ADMIN", "ROLE_MEMBERS"})
	@PutMapping("/member/withdraw")
	public ResponseEntity<Void> updateMemberStatusToWithdraw(@CurrentMembers Long memberId,
		@RequestBody DeleteMemberRequest deleteMemberRequest) {
		memberService.updateMemberStatusToWithdraw(memberId, deleteMemberRequest);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/find")
	public ResponseEntity<FindMemberLoginResponse> find(@Valid @RequestBody FindMemberRequest findMemberRequest) {
		FindMemberLoginResponse memberLoginResponse = memberService.findMembersLogin(findMemberRequest.loginId());
		return ResponseEntity.ok(memberLoginResponse);
	}

	@PostMapping("/payco")
	public ResponseEntity<GetPaycoMembersResponse> getPaycoMembers(
		@Valid @RequestBody GetPaycoMembersRequest getPaycoMembersRequest) {
		GetPaycoMembersResponse getPaycoMembersResponse = memberService.getPaycoMembers(getPaycoMembersRequest);
		return ResponseEntity.ok(getPaycoMembersResponse);
	}

	@CheckRole({"ROLE_ADMIN", "ROLE_MEMBERS"})
	@PostMapping("/payco/link")
	public ResponseEntity<Void> linkPayco(@Valid @RequestBody LinkPaycoMembersRequest linkPaycoMembersRequest) {
		memberService.linkPaycoMembers(linkPaycoMembersRequest);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/uuid")
	public ResponseEntity<GetMembersUUIDResponse> findUUID(@RequestHeader("Authorization") String authorization,
		@RequestBody GetMembersUUIDRequest getMembersUUIDRequest) {
		GetMembersUUIDResponse membersId = authMembersClient.getMembersId(getMembersUUIDRequest).getBody();

		return ResponseEntity.ok(membersId);
	}

	@PostMapping("/status")
	public ResponseEntity<GetDormantMembersResponse> getMemberDormantStatus(
		@Valid @RequestBody GetDormantMembersRequest getDormantMembersRequest) {
		GetDormantMembersResponse getDormantMembersResponse = memberService.getDormantMembers(getDormantMembersRequest);
		return ResponseEntity.ok(getDormantMembersResponse);
	}
}
