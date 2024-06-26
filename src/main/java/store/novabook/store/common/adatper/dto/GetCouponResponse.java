package store.novabook.store.common.adatper.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import store.novabook.store.common.adatper.CouponType;
import store.novabook.store.common.adatper.DiscountType;

@Builder
public record GetCouponResponse(Long id, CouponType type, String name, long discountAmount, DiscountType discountType,
								long maxDiscountAmount, long minPurchaseAmount, LocalDateTime createdAt,
								LocalDateTime expirationAt) {
}
