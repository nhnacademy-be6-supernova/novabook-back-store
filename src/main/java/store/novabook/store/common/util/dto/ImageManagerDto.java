package store.novabook.store.common.util.dto;

public record ImageManagerDto(
	String endpointUrl,
	String accessKey,
	String secretKey,
	String bucketName,
	String localStorage
) {
}
