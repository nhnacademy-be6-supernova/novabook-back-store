package store.novabook.store.book.dto.response;

import lombok.Builder;

@Builder
public record GetBookToMainResponse (
	Long id,
	String title,
	String image,
	Integer price,
	Integer discountPrice

){
}
