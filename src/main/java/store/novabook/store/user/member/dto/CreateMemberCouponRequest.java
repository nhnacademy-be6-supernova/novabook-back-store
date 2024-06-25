package store.novabook.store.user.member.dto;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record CreateMemberCouponRequest(
	Long couponId,
	Long memberId,
	LocalDateTime usedAt
) {
}
