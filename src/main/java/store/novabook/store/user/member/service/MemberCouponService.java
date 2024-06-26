package store.novabook.store.user.member.service;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import store.novabook.store.adatper.CouponAdapter;
import store.novabook.store.adatper.dto.CreateCouponRequest;
import store.novabook.store.adatper.dto.CreateCouponResponse;
import store.novabook.store.common.exception.EntityNotFoundException;
import store.novabook.store.user.member.dto.CreateMemberCouponRequest;
import store.novabook.store.user.member.dto.CreateMemberCouponResponse;
import store.novabook.store.user.member.entity.Member;
import store.novabook.store.user.member.entity.MemberCoupon;
import store.novabook.store.user.member.repository.MemberCouponRepository;
import store.novabook.store.user.member.repository.MemberRepository;

@RequiredArgsConstructor
@Service
@Transactional
public class MemberCouponService {
	private final MemberCouponRepository memberCouponRepository;
	private final MemberRepository memberRepository;
	private final CouponAdapter couponAdapter;

	public CreateMemberCouponResponse createMemberCoupon(Long memberId, CreateMemberCouponRequest request) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new EntityNotFoundException(Member.class));

		CreateCouponResponse couponResponse = couponAdapter.createCoupon(
			CreateCouponRequest.builder().couponTemplateId(request.couponTemplateId()).build()).getBody();

		MemberCoupon memberCoupon = MemberCoupon.builder().couponId(couponResponse.id()).member(member).build();
		memberCouponRepository.save(memberCoupon);

		return CreateMemberCouponResponse.fromEntity(memberCoupon);
	}

}

