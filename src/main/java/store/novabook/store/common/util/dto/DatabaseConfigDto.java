package store.novabook.store.common.util.dto;

public record DatabaseConfigDto(
	String url,
	String username,
	String password
) {
}
