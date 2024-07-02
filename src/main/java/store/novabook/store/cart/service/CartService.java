package store.novabook.store.cart.service;

import org.springframework.transaction.annotation.Transactional;

import store.novabook.store.cart.dto.request.CreateCartRequest;
import store.novabook.store.cart.dto.response.GetCartResponse;

public interface CartService {
	void createCart(CreateCartRequest createCartRequest);

	GetCartResponse getCartByMemberId(Long memberId);
}
