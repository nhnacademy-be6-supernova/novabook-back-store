package store.novabook.store.member.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record DownloadCouponRequest(@NotNull Long couponTemplateId) {
}
