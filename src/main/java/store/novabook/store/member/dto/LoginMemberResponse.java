package store.novabook.store.member.dto;

public record LoginMemberResponse(boolean success, Long memberId, String name) {
}
