package store.novabook.store.book.dto;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record BookResponse(
	Long id,
	Long bookStatusId,
	String isbn,
	String title,
	String subTitle,
	String engTitle,
	String index,
	String explanation,
	String translator,
	String publisher,
	LocalDateTime publicationDate,
	int inventory,
	long price,
	boolean isPackaged,
	String image,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {
}