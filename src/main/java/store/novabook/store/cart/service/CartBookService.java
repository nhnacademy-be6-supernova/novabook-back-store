package store.novabook.store.cart.service;

import store.novabook.store.cart.dto.request.CreateCartBookListRequest;
import store.novabook.store.cart.dto.request.CreateCartBookRequest;
import store.novabook.store.cart.dto.request.DeleteCartBookListRequest;
import store.novabook.store.cart.dto.request.UpdateCartBookQuantityRequest;
import store.novabook.store.cart.dto.response.CreateCartBookListResponse;
import store.novabook.store.cart.dto.response.CreateCartBookResponse;
import store.novabook.store.cart.dto.response.GetCartResponse;

public interface CartBookService {
	GetCartResponse getCartBookAll(Long cartId);
	GetCartResponse getCartBookAllByMemberId(Long memberId);
	CreateCartBookResponse createCartBook(Long memberId, CreateCartBookRequest createCartBookRequest);
	CreateCartBookListResponse createCartBooks(Long memberId, CreateCartBookListRequest createCartBookListRequest);
	void deleteCartBook(Long memberId, Long bookId);

	void deleteCartBooks(Long memberId, DeleteCartBookListRequest deleteCartBookListRequest);

	void updateCartBookQuantity(Long memberId, UpdateCartBookQuantityRequest request);
}
