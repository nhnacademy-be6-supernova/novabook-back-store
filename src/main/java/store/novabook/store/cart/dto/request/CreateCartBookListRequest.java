package store.novabook.store.cart.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateCartBookListRequest(
	@NotNull(message = "cartBookList가 null 값 입니다.")
	List<CreateCartBookRequest> cartBookList
) {}
