package store.novabook.store.category.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateCategoryRequest(
	@NotBlank(message = "카테고리 이름은 필수 입력값 입니다.")
	String name
) {
}
