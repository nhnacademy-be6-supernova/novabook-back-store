package store.novabook.store.user.member.dto;

import lombok.Builder;
import store.novabook.store.user.member.entity.MemberCoupon;

@Builder
public record CreateMemberCouponResponse(Long couponId) {
	public static CreateMemberCouponResponse fromEntity(MemberCoupon memberCoupon) {
		return CreateMemberCouponResponse.builder()
			.couponId(memberCoupon.getId())
			.build();
	}
}
