package store.novabook.store.cart.service;

import store.novabook.store.cart.dto.request.CreateCartBookRequest;
import store.novabook.store.cart.dto.response.CreateCartBookResponse;
import store.novabook.store.cart.dto.response.GetCartResponse;

public interface CartBookService {
	GetCartResponse getCartBookAll(Long cartId);
	GetCartResponse getCartBookAllByMemberId(Long memberId);
	CreateCartBookResponse createCartBook(CreateCartBookRequest createCartBookRequest);
	void deleteCartBook(Long cartBookId);
}
