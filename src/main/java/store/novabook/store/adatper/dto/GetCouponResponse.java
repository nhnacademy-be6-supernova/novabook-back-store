package store.novabook.store.adatper.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import store.novabook.store.adatper.CouponType;
import store.novabook.store.adatper.DiscountType;

@Builder
public record GetCouponResponse(Long id, CouponType type, String name, long discountAmount, DiscountType discountType,
								long maxDiscountAmount, long minPurchaseAmount, LocalDateTime createdAt,
								LocalDateTime expirationAt) {
}
