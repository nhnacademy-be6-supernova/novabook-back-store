package store.novabook.store.member.controller;

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
import store.novabook.store.common.security.aop.CheckRole;
import store.novabook.store.common.security.aop.CurrentMembers;
import store.novabook.store.member.controller.docs.MemberAddressControllerDocs;
import store.novabook.store.member.dto.request.CreateMemberAddressRequest;
import store.novabook.store.member.dto.request.UpdateMemberAddressRequest;
import store.novabook.store.member.dto.response.CreateMemberAddressResponse;
import store.novabook.store.member.dto.response.ExceedResponse;
import store.novabook.store.member.dto.response.GetMemberAddressListResponse;
import store.novabook.store.member.dto.response.GetMemberAddressResponse;
import store.novabook.store.member.service.MemberAddressService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/store/addresses")
public class MemberAddressController implements MemberAddressControllerDocs {

	private final MemberAddressService memberAddressService;

	@CheckRole({"ROLE_ADMIN", "ROLE_MEMBERS"})
	@PostMapping
	public ResponseEntity<CreateMemberAddressResponse> createMemberAddress(
		@Valid @RequestBody CreateMemberAddressRequest createMemberAddressRequest, @CurrentMembers Long memberId) {
		CreateMemberAddressResponse saved = memberAddressService.createMemberAddress(createMemberAddressRequest,
			memberId);
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}

	@CheckRole({"ROLE_ADMIN", "ROLE_MEMBERS"})
	@GetMapping
	public ResponseEntity<GetMemberAddressListResponse> getMemberAddressAll(@CurrentMembers Long memberId) {
		List<GetMemberAddressResponse> memberAddressAll = memberAddressService.getMemberAddressAll(memberId);
		GetMemberAddressListResponse getMemberAddressListResponse = GetMemberAddressListResponse.builder()
			.memberAddresses(memberAddressAll)
			.build();
		return ResponseEntity.ok(getMemberAddressListResponse);
	}

	@CheckRole({"ROLE_ADMIN", "ROLE_MEMBERS"})
	@GetMapping("/{memberAddressId}")
	public ResponseEntity<GetMemberAddressResponse> getMemberAddress(@PathVariable Long memberAddressId) {
		GetMemberAddressResponse memberAddressResponse = memberAddressService.getMemberAddress(memberAddressId);
		return ResponseEntity.ok(memberAddressResponse);
	}

	@CheckRole({"ROLE_ADMIN", "ROLE_MEMBERS"})
	@PutMapping("/{memberAddressId}")
	public ResponseEntity<Void> updateMemberAddress(@PathVariable Long memberAddressId,
		@RequestBody UpdateMemberAddressRequest updateMemberAddressRequest) {
		memberAddressService.updateMemberAddress(memberAddressId, updateMemberAddressRequest);
		return ResponseEntity.ok().build();
	}

	@CheckRole({"ROLE_ADMIN", "ROLE_MEMBERS"})
	@DeleteMapping("/{memberAddressId}")
	public ResponseEntity<Void> deleteMemberAddress(@PathVariable Long memberAddressId) {
		memberAddressService.deleteMemberAddress(memberAddressId);
		return ResponseEntity.ok().build();
	}

	@CheckRole({"ROLE_ADMIN", "ROLE_MEMBERS"})
	@GetMapping("/is-exceed")
	public ResponseEntity<ExceedResponse> isExceed(@CurrentMembers Long memberId) {
		ExceedResponse isExceedMemberAddressCount = memberAddressService.isExceed(memberId);
		return ResponseEntity.ok(isExceedMemberAddressCount);
	}

}
