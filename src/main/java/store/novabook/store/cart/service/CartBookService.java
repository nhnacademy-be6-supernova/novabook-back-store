package store.novabook.store.cart.service;

import store.novabook.store.cart.dto.CartBookDTO;
import store.novabook.store.cart.dto.CartBookIdDTO;
import store.novabook.store.cart.dto.CartBookListDTO;
import store.novabook.store.cart.dto.request.DeleteCartBookListRequest;
import store.novabook.store.cart.dto.request.GetBookInfoRequest;
import store.novabook.store.cart.dto.request.UpdateCartBookQuantityRequest;
import store.novabook.store.cart.dto.response.CreateCartBookListResponse;
import store.novabook.store.cart.dto.response.CreateCartBookResponse;
import store.novabook.store.cart.dto.response.GetBookInfoResponse;

public interface CartBookService {

	CartBookListDTO getCartBookAllByMemberId(Long memberId);

	CreateCartBookResponse createCartBook(Long memberId, CartBookDTO createCartBookRequest);

	CreateCartBookListResponse createCartBooks(Long memberId, CartBookListDTO createCartBookListRequest);

	void deleteCartBook(Long memberId, Long bookId);

	void deleteCartBooks(Long memberId, DeleteCartBookListRequest deleteCartBookListRequest);

	void updateCartBookQuantity(Long memberId, UpdateCartBookQuantityRequest request);

	CartBookListDTO getCartBookAllByGuest(CartBookIdDTO request);

	Integer getCartCount(Long memberId);
}
