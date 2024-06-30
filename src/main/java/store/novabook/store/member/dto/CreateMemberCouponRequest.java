package store.novabook.store.member.dto;

import lombok.Builder;

@Builder
public record CreateMemberCouponRequest(
	Long couponTemplateId
) {
}
