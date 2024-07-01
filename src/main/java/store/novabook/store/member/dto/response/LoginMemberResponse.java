package store.novabook.store.member.dto.response;

public record LoginMemberResponse(boolean success, Long memberId, String name) {
}
