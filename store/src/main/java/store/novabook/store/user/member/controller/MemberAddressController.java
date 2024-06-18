package store.novabook.store.user.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import store.novabook.store.user.member.dto.CreateMemberAddressRequest;
import store.novabook.store.user.member.entity.MemberAddress;
import store.novabook.store.user.member.service.MemberAddressService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/addresses")
public class MemberAddressController {

	private final MemberAddressService memberAddressService;

	@PostMapping
	public ResponseEntity<String> createMemberAddress(@RequestBody CreateMemberAddressRequest createMemberAddressRequest) {
		MemberAddress createMemberAddress = memberAddressService.createMemberAddress(createMemberAddressRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(createMemberAddress.toString());
	}

}
