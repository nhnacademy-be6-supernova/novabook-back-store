package store.novabook.store.user.member.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import store.novabook.store.user.member.dto.CreateMemberCouponRequest;
import store.novabook.store.user.member.dto.CreateMemberCouponResponse;
import store.novabook.store.user.member.service.MemberCouponService;

@RequiredArgsConstructor
@RequestMapping("/memberCoupons")
@RestController
public class MemberCouponController {
	private final MemberCouponService memberCouponService;

	public ResponseEntity<CreateMemberCouponResponse> createMemberCoupon(CreateMemberCouponRequest request) {
		CreateMemberCouponResponse saved = memberCouponService.createMemberCoupon(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}
}
