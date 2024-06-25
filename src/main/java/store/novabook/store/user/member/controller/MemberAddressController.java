package store.novabook.store.user.member.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.store.user.member.dto.CreateMemberAddressRequest;
import store.novabook.store.user.member.dto.CreateMemberAddressResponse;
import store.novabook.store.user.member.dto.GetMemberAddressListResponse;
import store.novabook.store.user.member.dto.GetMemberAddressResponse;
import store.novabook.store.user.member.dto.UpdateMemberAddressRequest;
import store.novabook.store.user.member.service.MemberAddressService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/store/addresses")
public class MemberAddressController {

	private final MemberAddressService memberAddressService;

	@PostMapping
	public ResponseEntity<CreateMemberAddressResponse> createMemberAddress(
		@Valid @RequestBody CreateMemberAddressRequest createMemberAddressRequest, @RequestHeader Long memberId) {
		CreateMemberAddressResponse saved = memberAddressService.createMemberAddress(createMemberAddressRequest,
			memberId);
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}

	@GetMapping
	public ResponseEntity<GetMemberAddressListResponse> getMemberAddressAll(@RequestHeader Long memberId) {
		List<GetMemberAddressResponse> memberAddressAll = memberAddressService.getMemberAddressAll(memberId);
		GetMemberAddressListResponse getMemberAddressListResponse = GetMemberAddressListResponse.builder()
			.memberAddresses(memberAddressAll)
			.build();
		return ResponseEntity.ok(getMemberAddressListResponse);
	}

	@GetMapping("/{memberAddressId}")
	public ResponseEntity<GetMemberAddressResponse> getMemberAddress(@PathVariable Long memberAddressId) {
		GetMemberAddressResponse memberAddressResponse = memberAddressService.getMemberAddress(memberAddressId);
		return ResponseEntity.ok(memberAddressResponse);
	}

	@PutMapping("/{memberAddressId}")
	public ResponseEntity<Void> updateMemberAddress(@PathVariable Long memberAddressId,
		@RequestBody UpdateMemberAddressRequest updateMemberAddressRequest) {
		memberAddressService.updateMemberAddress(memberAddressId, updateMemberAddressRequest);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{memberAddressId}")
	public ResponseEntity<Void> deleteMemberAddress(@PathVariable Long memberAddressId) {
		memberAddressService.deleteMemberAddress(memberAddressId);
		return ResponseEntity.ok().build();
	}

	@CrossOrigin(origins = "http://localhost:8080")
	@GetMapping("/checkMemberAddressCount")
	public ResponseEntity<Boolean> checkMemberAddressCount(@RequestHeader Long memberId) {
		boolean isExceedMemberAddressCount = memberAddressService.checkMemberAddressCount(memberId);
		return ResponseEntity.ok(isExceedMemberAddressCount);
	}

}
