package store.novabook.store.member.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import jakarta.validation.Valid;
import store.novabook.store.common.adatper.dto.GetCouponAllResponse;
import store.novabook.store.common.adatper.dto.GetCouponHistoryResponse;
import store.novabook.store.common.adatper.dto.GetUsedCouponHistoryResponse;
import store.novabook.store.common.messaging.dto.RegisterCouponMessage;
import store.novabook.store.member.dto.request.CreateMemberCouponRequest;
import store.novabook.store.member.dto.response.CreateMemberCouponResponse;
import store.novabook.store.member.dto.response.GetCouponIdsResponse;

public interface MemberCouponService {
	CreateMemberCouponResponse createMemberCoupon(Long memberId, CreateMemberCouponRequest request);

	void createMemberCouponByMessage(@Valid RegisterCouponMessage message);

	GetCouponIdsResponse getMemberCoupon(Long memberId);

	GetCouponAllResponse getValidAllByMemberId(Long memberId);

	Page<GetCouponHistoryResponse> getMemberCouponHistory(Long memberId, Pageable pageable);

	Page<GetUsedCouponHistoryResponse> getMemberUsedCouponHistory(Long memberId, Pageable pageable);
}
