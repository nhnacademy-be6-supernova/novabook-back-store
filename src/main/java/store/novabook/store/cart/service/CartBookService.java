package store.novabook.store.cart.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.book.entity.Book;
import store.novabook.store.book.repository.BookRepository;
import store.novabook.store.cart.dto.CreateCartBookRequest;
import store.novabook.store.cart.dto.DeleteCartBookRequest;
import store.novabook.store.cart.dto.GetCartBookResponse;
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
	private final BookRepository bookRepository;

	public void createCartBook(CreateCartBookRequest createCartBookRequest) {
		cartBookRepository.findByCartIdAndBookId(
				createCartBookRequest.cartId(),
				createCartBookRequest.bookId())
			.ifPresentOrElse(
				cartBook -> {
					cartBookRepository.save(
						CartBook.builder()
							.id(cartBook.getId())
							.cart(cartBook.getCart())
							.book(cartBook.getBook())
							.quantity(cartBook.getQuantity() + createCartBookRequest.quantity())
							.createdAt(cartBook.getCreatedAt())
							.updatedAt(LocalDateTime.now())
							.build());
				},
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
							.createdAt(LocalDateTime.now())
							.build());
				}
			);
	}

	@Transactional(readOnly = true)
	public Page<GetCartBookResponse> getCartBookListByCartId(Long cartId, Pageable pageable) {
		Page<CartBook> cartBooks = cartBookRepository.findAllByCartId(cartId, pageable)
			.orElseThrow(() -> new EntityNotFoundException(Cart.class, cartId));
		Page<GetCartBookResponse> cartBookResponses = cartBooks.map(GetCartBookResponse::fromEntity);

		return new PageImpl<>(cartBookResponses.getContent(), pageable, cartBooks.getTotalElements());
	}

	public void deleteCartBookAndBook(DeleteCartBookRequest deleteCartBookRequest) {
		cartBookRepository.findByCartIdAndBookId(deleteCartBookRequest.cartBookId(), deleteCartBookRequest.bookId())
			.orElseThrow(
				() -> new EntityNotFoundException(DeleteCartBookRequest.class, deleteCartBookRequest.cartBookId()));

		cartBookRepository.deleteByCartIdAndBookId(deleteCartBookRequest.cartBookId(), deleteCartBookRequest.bookId());
	}

}
