package store.novabook.store.common.adatper.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateCouponResponse(@NotNull Long id) {
}
