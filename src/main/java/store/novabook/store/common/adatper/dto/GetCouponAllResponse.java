package store.novabook.store.common.adatper.dto;

import java.util.List;

import lombok.Builder;

@Builder
public record GetCouponAllResponse(List<GetCouponResponse> couponResponseList) {
}
