package store.novabook.store.cart.dto.response;

import java.util.List;

public record GetBookInfoResponse(List<CartBookInfoDto> bookInfo) {
}
