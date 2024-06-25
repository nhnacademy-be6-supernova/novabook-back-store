package store.novabook.store.user.member.service;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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

	public CreateMemberCouponResponse createMemberCoupon(CreateMemberCouponRequest request) {
		Member member = memberRepository.findById(request.memberId())
			.orElseThrow(() -> new EntityNotFoundException(Member.class));

		MemberCoupon memberCoupon = MemberCoupon.builder()
			.id(request.couponId())
			.member(member)
			.usedAt(request.usedAt())
			.build();
		memberCouponRepository.save(memberCoupon);

		return CreateMemberCouponResponse.fromEntity(memberCoupon);

	}

}
