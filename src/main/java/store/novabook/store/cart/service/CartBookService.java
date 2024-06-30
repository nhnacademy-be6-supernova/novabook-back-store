package store.novabook.store.cart.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.book.entity.Book;
import store.novabook.store.book.repository.BookRepository;
import store.novabook.store.cart.dto.BookAndQuantityDTO;
import store.novabook.store.cart.dto.CreateCartBookRequest;
import store.novabook.store.cart.dto.DeleteCartBookRequest;
import store.novabook.store.cart.dto.GetCartBookListResponse;
import store.novabook.store.cart.dto.GetCartBookResponse;
import store.novabook.store.cart.entity.Cart;
import store.novabook.store.cart.entity.CartBook;
import store.novabook.store.cart.repository.CartBookRepository;
import store.novabook.store.cart.repository.CartRepository;
import store.novabook.store.common.exception.EntityNotFoundException;

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

	@Transactional(readOnly = true)
	public Page<GetCartBookResponse> getCartBookListByCartId(Long cartId, Pageable pageable) {
		Page<CartBook> cartBooks = cartBookRepository.findAllByCartId(cartId, pageable)
			.orElseThrow(() -> new EntityNotFoundException(Cart.class, cartId));
		Page<GetCartBookResponse> cartBookResponses = cartBooks.map(GetCartBookResponse::fromEntity);

		return new PageImpl<>(cartBookResponses.getContent(), pageable, cartBooks.getTotalElements());
	}

	public GetCartBookListResponse getCartBookListByCartId(Long cartId) {
		// 장바구니 ID에 해당하는 <장바구니카트>들 조회
		List<CartBook> cartBooks = cartBookRepository.findAllByCartId(cartId);

		// <장바구니도서>에서 도서, 수량을 묶은 List DTO 반환
		List<BookAndQuantityDTO> cartBookResponse = cartBooks.stream()
			.map(cartBook -> new BookAndQuantityDTO(cartBook.getBook().getId(), cartBook.getQuantity()))
			.toList();

		return GetCartBookListResponse.builder().cartBooks(cartBookResponse).build();
	}


	public void deleteCartBookAndBook(DeleteCartBookRequest deleteCartBookRequest) {
		if (!cartBookRepository.existsByCartIdAndBookId(deleteCartBookRequest.cartBookId(),
			deleteCartBookRequest.bookId())) {
			throw new EntityNotFoundException(CartBook.class, deleteCartBookRequest.cartBookId());
		}

		cartBookRepository.deleteByCartIdAndBookId(deleteCartBookRequest.cartBookId(), deleteCartBookRequest.bookId());
	}

}
