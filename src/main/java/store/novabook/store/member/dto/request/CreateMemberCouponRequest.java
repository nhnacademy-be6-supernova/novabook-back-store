package store.novabook.store.member.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateMemberCouponRequest(@NotNull(message = "쿠폰 템플릿 ID는 필수 입력 항목입니다.") Long couponTemplateId) {
}
