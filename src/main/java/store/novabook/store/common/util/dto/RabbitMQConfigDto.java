package store.novabook.store.common.util.dto;

public record RabbitMQConfigDto(
	String host,
	int port,
	String username,
	String password
) {
}
