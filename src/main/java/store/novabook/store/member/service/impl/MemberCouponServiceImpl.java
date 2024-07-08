package store.novabook.store.member.service.impl;

import java.util.LinkedList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import store.novabook.store.common.adatper.CouponAdapter;
import store.novabook.store.common.adatper.CouponStatus;
import store.novabook.store.common.adatper.dto.CreateCouponRequest;
import store.novabook.store.common.adatper.dto.CreateCouponResponse;
import store.novabook.store.common.adatper.dto.GetCouponAllResponse;
import store.novabook.store.common.adatper.dto.GetCouponHistoryResponse;
import store.novabook.store.common.adatper.dto.GetCouponResponse;
import store.novabook.store.common.adatper.dto.GetUsedCouponHistoryResponse;
import store.novabook.store.common.messaging.CouponSender;
import store.novabook.store.common.messaging.dto.CreateCouponNotifyMessage;
import store.novabook.store.common.messaging.dto.RegisterCouponMessage;
import store.novabook.store.common.response.ApiResponse;
import store.novabook.store.common.response.PageResponse;
import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.common.exception.NotFoundException;
import store.novabook.store.member.dto.request.CreateMemberCouponRequest;
import store.novabook.store.member.dto.request.DownloadCouponMessageRequest;
import store.novabook.store.member.dto.request.DownloadCouponRequest;
import store.novabook.store.member.dto.request.RegisterCouponRequest;
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
	private final CouponSender couponSender;

	@Override
	public CreateMemberCouponResponse createMemberCoupon(Long memberId, CreateMemberCouponRequest request) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

		CreateCouponResponse couponResponse = couponAdapter.createCoupon(
			CreateCouponRequest.builder().couponTemplateId(request.couponTemplateId()).build()).getBody();

		MemberCoupon memberCoupon = MemberCoupon.builder().couponId(couponResponse.id()).member(member).build();
		memberCouponRepository.save(memberCoupon);
		return CreateMemberCouponResponse.fromEntity(memberCoupon);
	}

	@Override
	public CreateMemberCouponResponse downloadCoupon(Long memberId, DownloadCouponRequest request) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

		List<Long> couponList = memberCouponRepository.findByMemberId(memberId)
			.stream()
			.map(MemberCoupon::getCouponId)
			.toList();

		CreateCouponResponse response = couponAdapter.createCoupon(
				CreateCouponRequest.builder().couponIdList(couponList).couponTemplateId(request.couponTemplateId()).build())
			.getBody();
		MemberCoupon saved = memberCouponRepository.save(
			MemberCoupon.builder().couponId(response.id()).member(member).build());

		return CreateMemberCouponResponse.fromEntity(saved);
	}

	@Override
	public void downloadLimitedCoupon(String token, String refresh, Long memberId,
		DownloadCouponMessageRequest request) {
		List<Long> couponList = memberCouponRepository.findByMemberId(memberId)
			.stream()
			.map(MemberCoupon::getCouponId)
			.toList();

		couponSender.sendToNotifyQueue(token, refresh,
			CreateCouponNotifyMessage.from(memberId, couponList, request));
	}


	@Override
	public CreateMemberCouponResponse registerMemberCoupon(Long memberId, RegisterCouponRequest request) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

		MemberCoupon memberCoupon = MemberCoupon.builder().member(member).couponId(request.couponId()).build();
		MemberCoupon saved = memberCouponRepository.save(memberCoupon);
		return CreateMemberCouponResponse.fromEntity(saved);
	}

	// 웰컴 쿠폰
	@Override
	public CreateMemberCouponResponse createMemberCouponByMessage(RegisterCouponMessage message) {
		Member member = memberRepository.findById(message.memberId())
			.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
		MemberCoupon memberCoupon = MemberCoupon.builder().member(member).couponId(message.couponId()).build();
		MemberCoupon saved = memberCouponRepository.save(memberCoupon);
		return CreateMemberCouponResponse.fromEntity(saved);
	}

	@Override
	public GetCouponAllResponse getValidAllByMemberId(Long memberId) {
		List<Long> couponIdList = memberCouponRepository.findByMemberId(memberId)
			.stream()
			.map(MemberCoupon::getCouponId)
			.toList();

		if (couponIdList.isEmpty()) {
			return GetCouponAllResponse.builder().build();
		}

		ApiResponse<GetCouponAllResponse> response = couponAdapter.getCouponValidAll(couponIdList);
		return response.getBody();
	}

	@Override
	public Page<GetCouponHistoryResponse> getMemberCouponHistory(Long memberId, Pageable pageable) {
		List<Long> couponList = memberCouponRepository.findByMemberId(memberId)
			.stream()
			.map(MemberCoupon::getCouponId)
			.toList();

		if (couponList.isEmpty()) {
			return new PageImpl<>(new LinkedList<>());
		}

		PageResponse<GetCouponResponse> couponResponse = couponAdapter.getCouponAll(couponList, pageable);
		return couponResponse.toPage().map(GetCouponHistoryResponse::fromEntity);
	}

	@Override
	public Page<GetUsedCouponHistoryResponse> getMemberUsedCouponHistory(Long memberId, Pageable pageable) {
		List<Long> couponList = memberCouponRepository.findByMemberId(memberId)
			.stream()
			.map(MemberCoupon::getCouponId)
			.toList();

		if (couponList.isEmpty()) {
			return new PageImpl<>(new LinkedList<>());
		}

		PageResponse<GetCouponResponse> response = couponAdapter.getCouponByStatus(couponList, CouponStatus.USED,
			pageable);

		return response.toPage().map(GetUsedCouponHistoryResponse::fromEntity);
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

