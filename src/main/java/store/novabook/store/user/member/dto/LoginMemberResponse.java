package store.novabook.store.user.member.dto;

public record LoginMemberResponse(boolean success, Long memberId, String name) {
}
