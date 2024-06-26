package store.novabook.store.user.member.dto;

import lombok.Builder;

@Builder
public record CreateMemberCouponRequest(
	Long couponId,
	Long memberId
) {
}
