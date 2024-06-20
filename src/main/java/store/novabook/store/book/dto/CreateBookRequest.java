package store.novabook.store.book.dto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotBlank;

public record CreateBookRequest(
	@NotBlank(message = "책 상태는 필수 입력값입니다.")
	Long bookStatusId,
	@NotBlank(message = "ISBN는 필수 입력값입니다.")
	String isbn,
	@NotBlank(message = "책 제목는 필수 입력값입니다.")
	String title,
	@NotBlank(message = "책 목차는 필수 입력값입니다.")
	String bookIndex,
	@NotBlank(message = "책 설명는 필수 입력값입니다.")
	String description,
	@NotBlank(message = "책 상세보기 설명은 필수 입력값입니다.")
	String descriptionDetail,
	@NotBlank(message = "책 작가는 필수 입력값입니다.")
	String author,
	@NotBlank(message = "책 출판사는 필수 입력값입니다.")
	String publisher,
	@NotBlank(message = "책 발행일는 필수 입력값입니다.")
	LocalDateTime publicationDate,
	@NotBlank(message = "책 재고는 필수 입력값입니다.")
	int inventory,
	@NotBlank(message = "책 가격은 필수 입력값입니다.")
	Long price,
	@NotBlank(message = "책 할인가격은 필수 입력값입니다.")
	Long discountPrice,
	@NotBlank(message = "책 상태는 필수 입력값입니다.")
	boolean isPackaged,
	String image,
	Set<Long> tags,
	HashMap<Integer, Long> category
) {
}
