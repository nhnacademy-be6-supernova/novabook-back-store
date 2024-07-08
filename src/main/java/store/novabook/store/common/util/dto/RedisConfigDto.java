package store.novabook.store.common.util.dto;

public record RedisConfigDto(
	String host,
	int database,
	String password,
	int port
) {
}
