package store.novabook.store.member.dto.request;

import lombok.Builder;

@Builder
public record CreateMemberCouponRequest(
	Long couponTemplateId
) {
}
