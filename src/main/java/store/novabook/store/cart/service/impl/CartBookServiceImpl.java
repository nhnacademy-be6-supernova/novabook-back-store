package store.novabook.store.cart.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.book.entity.Book;
import store.novabook.store.book.repository.BookRepository;
import store.novabook.store.cart.dto.request.CreateCartBookRequest;
import store.novabook.store.cart.dto.request.DeleteCartBookRequest;
import store.novabook.store.cart.dto.response.GetCartBookResponse;
import store.novabook.store.cart.entity.Cart;
import store.novabook.store.cart.entity.CartBook;
import store.novabook.store.cart.repository.CartBookRepository;
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

	@Override
	public void createCartBook(CreateCartBookRequest createCartBookRequest) {
		cartBookRepository.findByCartIdAndBookId(
				createCartBookRequest.cartId(),
				createCartBookRequest.bookId())
			.ifPresentOrElse(
				cartBook ->
					cartBookRepository.save(
						CartBook.builder()
							.cart(cartBook.getCart())
							.book(cartBook.getBook())
							.quantity(cartBook.getQuantity() + createCartBookRequest.quantity())
							.build())
				,
				() -> {
					Cart cart = cartRepository.findById(createCartBookRequest.cartId())
						.orElseThrow(() -> new EntityNotFoundException(Cart.class, createCartBookRequest.cartId()));

					Book book = bookRepository.findById(createCartBookRequest.bookId())
						.orElseThrow(() -> new EntityNotFoundException(Book.class, createCartBookRequest.bookId()));

					cartBookRepository.save(
						CartBook.builder()
							.cart(cart)
							.book(book)
							.quantity(createCartBookRequest.quantity())
							.build());
				}
			);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<GetCartBookResponse> getCartBookListByCartId(Long cartId, Pageable pageable) {
		Page<CartBook> cartBooks = cartBookRepository.findAllByCartId(cartId, pageable)
			.orElseThrow(() -> new EntityNotFoundException(Cart.class, cartId));
		// Page<GetCartBookResponse> cartBookResponses = cartBooks.map(GetCartBookResponse::fromEntity);

		return new PageImpl<>(null, pageable, cartBooks.getTotalElements());
	}

	@Override
	public void deleteCartBookAndBook(DeleteCartBookRequest deleteCartBookRequest) {
		if (!cartBookRepository.existsByCartIdAndBookId(deleteCartBookRequest.cartBookId(),
			deleteCartBookRequest.bookId())) {
			throw new EntityNotFoundException(CartBook.class, deleteCartBookRequest.cartBookId());
		}

		cartBookRepository.deleteByCartIdAndBookId(deleteCartBookRequest.cartBookId(), deleteCartBookRequest.bookId());
	}

}
