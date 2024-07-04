package store.novabook.store.member.service;

import org.springframework.data.domain.Pageable;

import jakarta.validation.Valid;
import store.novabook.store.common.adatper.dto.GetCouponAllResponse;
import store.novabook.store.common.adatper.dto.GetCouponHistoryAllResponse;
import store.novabook.store.common.adatper.dto.GetUsedCouponHistoryAllResponse;
import store.novabook.store.common.messaging.dto.RegisterCouponMessage;
import store.novabook.store.member.dto.request.CreateMemberCouponRequest;
import store.novabook.store.member.dto.response.CreateMemberCouponResponse;

public interface MemberCouponService {
	CreateMemberCouponResponse createMemberCoupon(Long memberId, CreateMemberCouponRequest request);

	void createMemberCouponByMessage(@Valid RegisterCouponMessage message);

	GetCouponAllResponse getValidAllByMemberId(Long memberId);

	GetCouponHistoryAllResponse getMemberCouponHistory(Long memberId, Pageable pageable);

	GetUsedCouponHistoryAllResponse getMemberUsedCouponHistory(Long memberId, Pageable pageable);
}
