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

import lombok.RequiredArgsConstructor;
import store.novabook.store.user.member.dto.CreateMemberAddressRequest;
import store.novabook.store.user.member.dto.CreateMemberAddressResponse;
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
		@RequestBody CreateMemberAddressRequest createMemberAddressRequest) {
		CreateMemberAddressResponse saved = memberAddressService.createMemberAddress(createMemberAddressRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}

	@GetMapping
	public ResponseEntity<List<GetMemberAddressResponse>> getMemberAddressAll() {
		List<GetMemberAddressResponse> memberAddressAll = memberAddressService.getMemberAddressAll();
		return ResponseEntity.ok(memberAddressAll);
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

}
