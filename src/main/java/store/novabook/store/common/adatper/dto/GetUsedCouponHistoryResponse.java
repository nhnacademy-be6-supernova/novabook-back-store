package store.novabook.store.common.adatper.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import store.novabook.store.common.adatper.CouponStatus;
import store.novabook.store.common.adatper.CouponType;
import store.novabook.store.common.adatper.DiscountType;

@Builder
public record GetUsedCouponHistoryResponse(LocalDateTime usedAt, String name, CouponType type, CouponStatus status,
										   long discountAmount, DiscountType discountType) {
	public static GetUsedCouponHistoryResponse fromEntity(GetCouponResponse couponResponse) {
		return GetUsedCouponHistoryResponse.builder()
			.usedAt(couponResponse.usedAt())
			.name(couponResponse.name())
			.type(couponResponse.type())
			.status(couponResponse.status())
			.discountAmount(couponResponse.discountAmount())
			.discountType(couponResponse.discountType())
			.build();
	}
}
