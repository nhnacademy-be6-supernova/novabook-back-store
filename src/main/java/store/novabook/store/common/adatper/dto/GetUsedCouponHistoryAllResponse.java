package store.novabook.store.common.adatper.dto;

import java.util.List;

import lombok.Builder;

@Builder
public record GetUsedCouponHistoryAllResponse(List<GetUsedCouponHistoryResponse> couponList) {
	public static GetUsedCouponHistoryAllResponse fromEntity(GetCouponAllResponse response) {
		List<GetCouponResponse> couponResponseList = response.couponResponseList();
		return GetUsedCouponHistoryAllResponse.builder()
			.couponList(couponResponseList.stream().map(GetUsedCouponHistoryResponse::fromEntity).toList())
			.build();
	}
}
