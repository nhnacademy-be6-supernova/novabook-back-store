package store.novabook.store.user.member.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import store.novabook.store.common.adatper.CouponAdapter;
import store.novabook.store.common.adatper.dto.CreateCouponRequest;
import store.novabook.store.common.adatper.dto.CreateCouponResponse;
import store.novabook.store.common.exception.EntityNotFoundException;
import store.novabook.store.message.CouponCreatedMessage;
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

	@RabbitListener(queues = "${rabbitmq.queue.member-coupon}")
	public void handleCouponCreatedMessage(CouponCreatedMessage message) {
		Member member = memberRepository.findById(message.memberId())
			.orElseThrow(() -> new EntityNotFoundException(Member.class));
		MemberCoupon memberCoupon = MemberCoupon.builder().member(member).couponId(message.couponId()).build();
		memberCouponRepository.save(memberCoupon);
	}

}

