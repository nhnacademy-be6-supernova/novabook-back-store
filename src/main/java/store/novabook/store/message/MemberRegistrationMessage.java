package store.novabook.store.message;

import lombok.Builder;

@Builder
public record MemberRegistrationMessage(Long memberId) {
}
