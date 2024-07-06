package store.novabook.store.common.adatper.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateCouponRequest(List<Long> couponIdList,
								  @NotNull(message = "쿠폰 코드는 필수 입력 항목입니다.") Long couponTemplateId) {
}
