package store.novabook.store.common.util.dto;

import lombok.Builder;

@Builder
public record ImageManagerDto(
	String endpointUrl,
	String accessKey,
	String secretKey,
	String bucketName,
	String localStorage
) {
}
