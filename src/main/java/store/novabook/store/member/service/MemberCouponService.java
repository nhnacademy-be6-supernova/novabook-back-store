package store.novabook.store.member.service;

import jakarta.validation.Valid;
import store.novabook.store.common.messaging.dto.RegisterCouponMessage;
import store.novabook.store.member.dto.request.CreateMemberCouponRequest;
import store.novabook.store.member.dto.response.CreateMemberCouponResponse;

public interface MemberCouponService {
	CreateMemberCouponResponse createMemberCoupon(Long memberId, CreateMemberCouponRequest request);

	void createMemberCouponByMessage(@Valid RegisterCouponMessage message);
}
