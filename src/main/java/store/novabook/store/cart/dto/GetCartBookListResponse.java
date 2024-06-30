package store.novabook.store.cart.dto;

import java.util.List;

import lombok.Builder;

@Builder
public record GetCartBookListResponse(
	List<BookAndQuantityDTO> cartBooks
) {}