package store.novabook.store.member.dto.response;

import lombok.Builder;
import store.novabook.store.member.entity.MemberCoupon;

@Builder
public record CreateMemberCouponResponse(Long couponId) {
	public static CreateMemberCouponResponse fromEntity(MemberCoupon memberCoupon) {
		return CreateMemberCouponResponse.builder()
			.couponId(memberCoupon.getCouponId())
			.build();
	}
}
