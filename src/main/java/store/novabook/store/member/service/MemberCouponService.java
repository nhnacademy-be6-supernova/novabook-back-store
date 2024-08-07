package store.novabook.store.member.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import store.novabook.store.common.adatper.dto.GetCouponAllResponse;
import store.novabook.store.common.adatper.dto.GetCouponHistoryResponse;
import store.novabook.store.common.adatper.dto.GetUsedCouponHistoryResponse;
import store.novabook.store.common.messaging.dto.RegisterCouponMessage;
import store.novabook.store.member.dto.request.CreateMemberCouponRequest;
import store.novabook.store.member.dto.request.DownloadCouponMessageRequest;
import store.novabook.store.member.dto.request.DownloadCouponRequest;
import store.novabook.store.member.dto.request.RegisterCouponRequest;
import store.novabook.store.member.dto.response.CreateMemberCouponResponse;
import store.novabook.store.member.dto.response.GetCouponIdsResponse;

public interface MemberCouponService {
	CreateMemberCouponResponse createMemberCoupon(Long memberId, CreateMemberCouponRequest request);

	GetCouponIdsResponse getMemberCoupon(Long memberId);

	CreateMemberCouponResponse registerMemberCoupon(Long memberId, RegisterCouponRequest request);

	GetCouponAllResponse getValidAllByMemberId(Long memberId);

	Page<GetCouponHistoryResponse> getMemberCouponHistory(Long memberId, Pageable pageable);

	Page<GetUsedCouponHistoryResponse> getMemberUsedCouponHistory(Long memberId, Pageable pageable);

	CreateMemberCouponResponse downloadCoupon(Long memberId, DownloadCouponRequest request);

	void downloadLimitedCoupon(String token, String refresh, Long memberId, DownloadCouponMessageRequest request);

	CreateMemberCouponResponse createMemberCouponByMessage(RegisterCouponMessage message);
}
