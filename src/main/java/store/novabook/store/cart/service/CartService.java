package store.novabook.store.cart.service;

import store.novabook.store.cart.dto.response.CartIdResponse;

public interface CartService {

	CartIdResponse getCartIdByMemberId(Long memberId);

	CartIdResponse createCartId(Long memberId);
}
