package store.novabook.store.common.adatper.dto;

import java.util.List;

import lombok.Builder;

@Builder
public record GetCouponHistoryAllResponse(List<GetCouponHistoryResponse> couponList) {
	public static GetCouponHistoryAllResponse fromEntity(GetCouponAllResponse response) {
		List<GetCouponResponse> couponResponseList = response.couponResponseList();
		return GetCouponHistoryAllResponse.builder()
			.couponList(couponResponseList.stream().map(GetCouponHistoryResponse::fromEntity).toList())
			.build();
	}
}
