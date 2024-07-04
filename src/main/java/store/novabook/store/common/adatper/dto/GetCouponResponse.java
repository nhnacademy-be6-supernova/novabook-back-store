package store.novabook.store.common.adatper.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import store.novabook.store.common.adatper.CouponStatus;
import store.novabook.store.common.adatper.CouponType;
import store.novabook.store.common.adatper.DiscountType;

/**
 * {@code GetCouponResponse} 레코드는 쿠폰 조회 응답을 나타냅니다.
 *
 * @param id                쿠폰 ID
 * @param type              쿠폰 타입
 * @param status            쿠폰 상태
 * @param name              쿠폰 이름
 * @param discountAmount    할인 금액
 * @param discountType      할인 유형
 * @param maxDiscountAmount 최대 할인 금액
 * @param minPurchaseAmount 최소 구매 금액
 * @param createdAt         쿠폰 생성 날짜
 * @param expirationAt      쿠폰 만료 날짜
 * @param usedAt            쿠폰 사용 날짜
 */
@Builder
public record GetCouponResponse(
	Long id,
	CouponType type,
	CouponStatus status,
	String name,
	long discountAmount,
	DiscountType discountType,
	long maxDiscountAmount,
	long minPurchaseAmount,
	LocalDateTime createdAt,
	LocalDateTime expirationAt,
	LocalDateTime usedAt) {
}
