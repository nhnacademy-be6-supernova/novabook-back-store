package store.novabook.store.member.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.store.common.adatper.CouponAdapter;
import store.novabook.store.common.adatper.dto.CreateCouponRequest;
import store.novabook.store.common.adatper.dto.CreateCouponResponse;
import store.novabook.store.common.exception.EntityNotFoundException;
import store.novabook.store.common.messaging.dto.RegisterCouponMessage;
import store.novabook.store.member.dto.request.CreateMemberCouponRequest;
import store.novabook.store.member.dto.response.CreateMemberCouponResponse;
import store.novabook.store.member.dto.response.GetCouponIdsResponse;
import store.novabook.store.member.entity.Member;
import store.novabook.store.member.entity.MemberCoupon;
import store.novabook.store.member.repository.MemberCouponRepository;
import store.novabook.store.member.repository.MemberRepository;
import store.novabook.store.member.service.MemberCouponService;

@RequiredArgsConstructor
@Service
@Transactional
public class MemberCouponServiceImpl implements MemberCouponService {

	private final MemberCouponRepository memberCouponRepository;
	private final MemberRepository memberRepository;
	private final CouponAdapter couponAdapter;

	@Override
	public CreateMemberCouponResponse createMemberCoupon(Long memberId, CreateMemberCouponRequest request) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new EntityNotFoundException(Member.class));

		CreateCouponResponse couponResponse = couponAdapter.createCoupon(
			CreateCouponRequest.builder().couponTemplateId(request.couponTemplateId()).build()).getBody();

		MemberCoupon memberCoupon = MemberCoupon.builder().couponId(couponResponse.id()).member(member).build();
		memberCouponRepository.save(memberCoupon);

		return CreateMemberCouponResponse.fromEntity(memberCoupon);
	}

	@Override
	public void createMemberCouponByMessage(@Valid RegisterCouponMessage message) {
		Member member = memberRepository.findById(message.memberId())
			.orElseThrow(() -> new EntityNotFoundException(Member.class));
		MemberCoupon memberCoupon = MemberCoupon.builder().member(member).couponId(message.couponId()).build();
		memberCouponRepository.save(memberCoupon);
	}

	@Override
	public GetCouponIdsResponse getMemberCoupon(Long memberId) {
		List<Long> couponIds = memberCouponRepository.findByMemberId(memberId)
			.stream()
			.map(MemberCoupon::getCouponId)
			.toList();

		return GetCouponIdsResponse.builder().couponIds(couponIds).build();
	}

}

