package store.novabook.store.common.messaging.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record RegisterCouponMessage(@NotNull Long memberId, @NotNull Long couponId) {
}
