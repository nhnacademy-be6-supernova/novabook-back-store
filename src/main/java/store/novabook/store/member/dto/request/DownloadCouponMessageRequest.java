package store.novabook.store.member.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import store.novabook.store.common.adatper.CouponType;

@Builder
public record DownloadCouponMessageRequest(@NotNull String uuid, @NotNull CouponType couponType,
										   @NotNull Long couponTemplateId) {
}
