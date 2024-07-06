package store.novabook.store.book.dto.request;

import lombok.Builder;

@Builder
public record ReviewImageDTO(
	String fileName,
	String fileType,
	String data
) {
}
