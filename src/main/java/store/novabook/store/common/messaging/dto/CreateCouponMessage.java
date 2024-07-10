package store.novabook.store.common.messaging.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import store.novabook.store.common.adatper.CouponType;

@Builder
public record CreateCouponMessage(@NotNull Long memberId, @NotNull List<Long> couponIdList,
								  @NotNull CouponType couponType, Long couponTemplateId) {
	public static CreateCouponMessage fromEntity(Long memberId,List<Long> couponIdList, CouponType couponType, Long couponTemplateId) {
		return CreateCouponMessage.builder()
			.memberId(memberId)
			.couponIdList(couponIdList)
			.couponType(couponType)
			.couponTemplateId(couponTemplateId)
			.build();
	}
}
