package store.novabook.store.cart.dto.request;

import java.util.List;

public record GetBookInfoRequest (List<Long> bookIds) {
}
