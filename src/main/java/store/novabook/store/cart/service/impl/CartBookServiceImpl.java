package store.novabook.store.cart.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.book.entity.Book;
import store.novabook.store.book.repository.BookRepository;
import store.novabook.store.cart.dto.request.CreateCartBookRequest;
import store.novabook.store.cart.dto.response.CreateCartBookResponse;
import store.novabook.store.cart.dto.response.GetCartResponse;
import store.novabook.store.cart.entity.Cart;
import store.novabook.store.cart.entity.CartBook;
import store.novabook.store.cart.repository.CartBookRepository;
import store.novabook.store.cart.repository.CartQueryRepository;
import store.novabook.store.cart.repository.CartRepository;
import store.novabook.store.cart.service.CartBookService;
import store.novabook.store.common.exception.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class CartBookServiceImpl implements CartBookService {

	private final CartRepository cartRepository;
	private final CartBookRepository cartBookRepository;
	private final BookRepository bookRepository;
	private final CartQueryRepository queryRepository;

	@Override
	public CreateCartBookResponse createCartBook(CreateCartBookRequest createCartBookRequest) {
		Cart cart = cartRepository.findById(createCartBookRequest.cartId())
			.orElseThrow(() -> new EntityNotFoundException(Cart.class, createCartBookRequest.cartId()));

		Book book = bookRepository.findById(createCartBookRequest.bookId())
			.orElseThrow(() -> new EntityNotFoundException(Book.class, createCartBookRequest.bookId()));

		Optional<CartBook> cartBook = cartBookRepository.findByCartIdAndBookId(
			createCartBookRequest.cartId(),
			createCartBookRequest.bookId());

		if (cartBook.isPresent()) {
			CartBook newCartbook = cartBookRepository.save(
				CartBook.builder()
					.cart(cartBook.get().getCart())
					.book(cartBook.get().getBook())
					.quantity(cartBook.get().getQuantity() + createCartBookRequest.quantity())
					.build());
			return new CreateCartBookResponse(newCartbook.getId());
		}else{
			CartBook newCartbook = cartBookRepository.save(
				CartBook.builder()
					.cart(cart)
					.book(book)
					.quantity(createCartBookRequest.quantity())
					.build());
			return new CreateCartBookResponse(newCartbook.getId());
		}
	}

	@Override
	public void deleteCartBook(Long cartBookId) {
		CartBook cartBook = cartBookRepository.findById(cartBookId).orElseThrow(() -> new EntityNotFoundException(CartBook.class, cartBookId));
		cartBook.updateIsExposed(false);

	}

	@Override
	@Transactional(readOnly = true)
	public GetCartResponse getCartBookAll(Long cartId) {
		return queryRepository.getCartBookAll(cartId);
	}

	@Override
	public GetCartResponse getCartBookAllByMemberId(Long memberId) {
		return queryRepository.getCartBookAllByMemberId(memberId);
	}

}
