package store.novabook.store.member.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import store.novabook.store.common.adatper.dto.GetCouponAllResponse;
import store.novabook.store.common.adatper.dto.GetCouponHistoryResponse;
import store.novabook.store.common.adatper.dto.GetUsedCouponHistoryResponse;
import store.novabook.store.common.security.aop.CurrentMembers;
import store.novabook.store.member.controller.docs.MemberCouponControllerDocs;
import store.novabook.store.member.dto.request.CreateMemberCouponRequest;
import store.novabook.store.member.dto.response.CreateMemberCouponResponse;
import store.novabook.store.member.dto.response.GetCouponIdsResponse;
import store.novabook.store.member.service.MemberCouponService;

@RequiredArgsConstructor
@RequestMapping("/api/v1/store/members/coupons")
@RestController
public class MemberCouponController implements MemberCouponControllerDocs {

	private final MemberCouponService memberCouponService;

	@PostMapping
	public ResponseEntity<CreateMemberCouponResponse> createMemberCoupon(@CurrentMembers Long memberId,
		@RequestBody CreateMemberCouponRequest request) {
		CreateMemberCouponResponse saved = memberCouponService.createMemberCoupon(memberId, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}

	@GetMapping("/history")
	public ResponseEntity<Page<GetCouponHistoryResponse>> getMemberCouponHistoryByMemberId(
		@CurrentMembers Long memberId,
		@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
		Page<GetCouponHistoryResponse> response = memberCouponService.getMemberCouponHistory(memberId, pageable);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/history/used")
	public ResponseEntity<Page<GetUsedCouponHistoryResponse>> getMemberUsedCouponHistoryByMemberId(
		@CurrentMembers Long memberId,
		@PageableDefault(sort = "usedAt", direction = Sort.Direction.DESC) Pageable pageable) {
		Page<GetUsedCouponHistoryResponse> response = memberCouponService.getMemberUsedCouponHistory(memberId,
			pageable);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/is-valid")
	public ResponseEntity<GetCouponAllResponse> getMemberCouponByMemberId(@CurrentMembers Long memberId) {
		GetCouponAllResponse response = memberCouponService.getValidAllByMemberId(memberId);
		return ResponseEntity.ok(response);
	}

	@GetMapping
	public ResponseEntity<GetCouponIdsResponse> getMemberCoupon(@CurrentMembers Long memberId) {
		return ResponseEntity.ok().body(memberCouponService.getMemberCoupon(memberId));
	}
}
