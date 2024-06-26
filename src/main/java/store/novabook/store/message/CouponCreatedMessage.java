package store.novabook.store.message;

import jakarta.validation.constraints.NotNull;

public record CouponCreatedMessage(@NotNull Long couponId, @NotNull Long memberId) {
}
