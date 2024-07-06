package store.novabook.store.book.dto.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateReviewRequest(
	@NotBlank(message = "content 값은 필수 입니다 ")
	String content,
	@NotNull(message = "score는 필수 값입니다.")
	Integer score,
	List<ReviewImageDTO> reviewImageDTOs
) {
}