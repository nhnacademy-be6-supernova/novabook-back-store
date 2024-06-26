package store.novabook.store.adatper.dto;

import java.util.List;

import lombok.Builder;

@Builder
public record GetCouponAllResponse(List<GetCouponResponse> couponResponseList) {
}
