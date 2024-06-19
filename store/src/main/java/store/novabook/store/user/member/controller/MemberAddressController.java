/*
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
import store.novabook.store.user.member.entity.MemberAddress;
import store.novabook.store.user.member.service.MemberAddressService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/addresses")
public class MemberAddressController {

	private final MemberAddressService memberAddressService;

	@PostMapping
	public ResponseEntity<CreateMemberAddressResponse> createMemberAddress(@RequestBody CreateMemberAddressRequest createMemberAddressRequest) {
		MemberAddress saved = memberAddressService.createMemberAddress(createMemberAddressRequest);
		return  ResponseEntity.status(HttpStatus.CREATED).body(CreateMemberAddressResponse.fromEntity(saved));
	}

	@GetMapping
	public ResponseEntity<GetMemberAddressResponse> getMemberAddressAll() {
		List<GetMemberAddressResponse> memberAddressAll = memberAddressService.getMemberAddressAll();
		return ResponseEntity.ok(memberAddressAll);
	}

	@GetMapping("/{memberAddressId}")
	public ResponseEntity<GetMemberAddressResponse> getMemberAddress(@PathVariable Long memberAddressId) {
		GetMemberAddressResponse memberAddressResponse = memberAddressService.getMemberAddress(memberAddressId);
		return ResponseEntity.ok(memberAddressResponse);
	}

	@PutMapping("/{memberAddressId}")
	public ResponseEntity<MemberAddress> updateMemberAddress(@PathVariable Long memberAddressId, @RequestBody CreateMemberAddressRequest createMemberAddressRequest) {
		memberAddressService.updateMemberAddress(memberAddressId, createMemberAddressRequest);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{memeberAddressId}")
	public ResponseEntity<Void> deleteMemberAddress(@PathVariable Long memeberAddressId) {
		memberAddressService.deleteMemberAddress(memeberAddressId);
		return ResponseEntity.ok().build();
	}



}
*/
