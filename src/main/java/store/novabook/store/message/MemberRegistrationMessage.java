package store.novabook.store.message;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record MemberRegistrationMessage(@NotNull Long memberId) {
}
