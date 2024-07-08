package store.novabook.store.common.messaging.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import store.novabook.store.common.adatper.CouponType;
import store.novabook.store.member.dto.request.DownloadCouponMessageRequest;

@Builder
public record CreateCouponNotifyMessage(@NotNull String uuid, @NotNull Long memberId, @NotNull List<Long> couponIdList,
										@NotNull CouponType couponType, @NotNull Long couponTemplateId) {
	public static CreateCouponNotifyMessage from(Long memberId, List<Long> couponIdList,
		DownloadCouponMessageRequest request) {
		return CreateCouponNotifyMessage.builder()
			.memberId(memberId)
			.couponIdList(couponIdList)
			.uuid(request.uuid())
			.couponType(request.couponType())
			.couponTemplateId(request.couponTemplateId())
			.build();
	}
}
