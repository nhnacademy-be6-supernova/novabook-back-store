package store.novabook.store.common.security.dto;

public record GetNewTokenRequest(
	String refreshToken
) {
}
