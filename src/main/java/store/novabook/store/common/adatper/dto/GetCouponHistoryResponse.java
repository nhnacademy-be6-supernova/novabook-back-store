package store.novabook.store.common.adatper.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import store.novabook.store.common.adatper.CouponStatus;
import store.novabook.store.common.adatper.CouponType;
import store.novabook.store.common.adatper.DiscountType;

@Builder
public record GetCouponHistoryResponse(LocalDateTime createdAt, String name, CouponType type, CouponStatus status,
									   long discountAmount, DiscountType discountType) {
	public static GetCouponHistoryResponse fromEntity(GetCouponResponse response) {
		return GetCouponHistoryResponse.builder()
			.createdAt(response.createdAt())
			.name(response.name())
			.type(response.type())
			.status(response.status())
			.discountAmount(response.discountAmount())
			.discountType(response.discountType())
			.build();
	}
}
