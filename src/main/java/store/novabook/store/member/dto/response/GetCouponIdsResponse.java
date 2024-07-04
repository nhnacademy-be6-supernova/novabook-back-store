package store.novabook.store.member.dto.response;

import java.util.List;

import lombok.Builder;

@Builder
public record GetCouponIdsResponse(List<Long> couponIds) {
}
