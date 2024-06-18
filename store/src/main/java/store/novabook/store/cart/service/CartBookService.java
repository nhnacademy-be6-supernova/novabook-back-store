package store.novabook.store.cart.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.cart.dto.CreateCartBookRequest;
import store.novabook.store.cart.entity.Cart;
import store.novabook.store.cart.entity.CartBook;
import store.novabook.store.cart.repository.CartBookRepository;
import store.novabook.store.cart.repository.CartRepository;
import store.novabook.store.exception.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class CartBookService {

	private final CartRepository cartRepository;
	private final CartBookRepository cartBookRepository;

	// CartBook cartBook = cartBookRepository.findByCart_IdAndBook_Id(
	// 		createCartBookRequest.cart().getId(), createCartBookRequest.book().getId())
	// 	.orElse(null);
	//
	// if (Objects.nonNull(cartBook)) {
	// 	cartBookRepository.updateQuantityById(cartBook.getId(), createCartBookRequest.quantity());
	// 	return;
	// }
	/*
	 * 1. 회원당 장바구니 여러개
	 * 2. 장바구니 안에 물건이 있는데 물건이 둘어옴
	 * 3. 장바구니가 있을 수 있음.
	 * */
	public void createCartBook(CreateCartBookRequest createCartBookRequest) {

		// Optional<CartBook> cartBookOptional = cartBookRepository.findByCart_IdAndBook_Id(
		// 	createCartBookRequest.cart().getId(), createCartBookRequest.book().getId()).orElseGet(() -> Optional.empty();

		// if (cartBookOptional.isPresent()) {
		// 	cartBookRepository.updateQuantityById(cartBookOptional.get().getId(), createCartBookRequest.quantity());
		// 	return;
		// }

		cartBookRepository.save(
			CartBook.builder()
				.cart(createCartBookRequest.cart())
				.book(createCartBookRequest.book())
				.quantity(createCartBookRequest.quantity())
				.build());
	}

	public List<CartBook> getCartBookListByCartId(Long cartId) {
		return cartBookRepository.findAllByCart_Id(cartId)
			.orElseThrow(() -> new EntityNotFoundException(Cart.class, cartId));
	}

	public void deleteCartBook(Long cartBookId) {
		cartBookRepository.findById(cartBookId)
			.orElseThrow(() -> new EntityNotFoundException(CartBook.class, cartBookId));

		cartBookRepository.deleteById(cartBookId);
	}

}
