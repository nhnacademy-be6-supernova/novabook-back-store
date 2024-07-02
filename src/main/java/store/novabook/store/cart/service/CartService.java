package store.novabook.store.cart.service;

import org.springframework.transaction.annotation.Transactional;

import store.novabook.store.cart.dto.CreateCartRequest;
import store.novabook.store.cart.dto.GetCartResponse;

public interface CartService {
	void createCart(CreateCartRequest createCartRequest);

	@Transactional(readOnly = true)
	GetCartResponse getCartByMemberId(Long memberId);
}
