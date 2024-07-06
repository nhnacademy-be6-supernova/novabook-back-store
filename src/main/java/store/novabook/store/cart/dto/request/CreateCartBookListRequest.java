package store.novabook.store.cart.dto.request;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import store.novabook.store.cart.dto.response.GetCartBookResponse;

@Builder
public record CreateCartBookListRequest(
	List<CreateCartBookRequest> cartBookList
) {}
