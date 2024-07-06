package store.novabook.store.cart.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.book.entity.Book;
import store.novabook.store.book.repository.BookRepository;
import store.novabook.store.cart.dto.request.CreateCartBookListRequest;
import store.novabook.store.cart.dto.request.CreateCartBookRequest;
import store.novabook.store.cart.dto.request.DeleteCartBookListRequest;
import store.novabook.store.cart.dto.response.CreateCartBookListResponse;
import store.novabook.store.cart.dto.response.CreateCartBookResponse;
import store.novabook.store.cart.dto.response.GetCartResponse;
import store.novabook.store.cart.entity.Cart;
import store.novabook.store.cart.entity.CartBook;
import store.novabook.store.cart.repository.CartBookRepository;
import store.novabook.store.cart.repository.CartQueryRepository;
import store.novabook.store.cart.repository.CartRepository;
import store.novabook.store.cart.service.CartBookService;
import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.common.exception.NotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class CartBookServiceImpl implements CartBookService {

	private final CartRepository cartRepository;
	private final CartBookRepository cartBookRepository;
	private final BookRepository bookRepository;
	private final CartQueryRepository queryRepository;

	@Override
	public CreateCartBookResponse createCartBook(Long memberId, CreateCartBookRequest createCartBookRequest) {
		CartBook cartBook = queryRepository.createCartBook(memberId, createCartBookRequest);
		return new CreateCartBookResponse(cartBookRepository.save(cartBook).getId());


		// Cart cart = cartRepository.findById(createCartBookRequest.cartId())
		// 	.orElseThrow(() -> new EntityNotFoundException(Cart.class, createCartBookRequest.cartId()));
		//
		// Book book = bookRepository.findById(createCartBookRequest.bookId())
		// 	.orElseThrow(() -> new EntityNotFoundException(Book.class, createCartBookRequest.bookId()));
		//
		// Optional<CartBook> cartBook = cartBookRepository.findByCartIdAndBookId(
		// 	createCartBookRequest.cartId(),
		// 	createCartBookRequest.bookId());
		//
		// if (cartBook.isPresent()) {
		// 	CartBook newCartbook = cartBookRepository.save(
		// 		CartBook.builder()
		// 			.cart(cartBook.get().getCart())
		// 			.book(cartBook.get().getBook())
		// 			.quantity(cartBook.get().getQuantity() + createCartBookRequest.quantity())
		// 			.build());
		// 	return new CreateCartBookResponse(newCartbook.getId());
		// }else{
		// 	CartBook newCartbook = cartBookRepository.save(
		// 		CartBook.builder()
		// 			.cart(cart)
		// 			.book(book)
		// 			.quantity(createCartBookRequest.quantity())
		// 			.build());
		// 	return new CreateCartBookResponse(newCartbook.getId());
		// }
	}
	@Override
	public CreateCartBookListResponse createCartBooks( Long memberId, CreateCartBookListRequest createCartBookListRequest) {
		// 카트 존재 여부 확인
		Cart cart = cartRepository.findByMemberId(memberId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.CART_NOT_FOUND));

		// 책 ID 목록 생성
		List<Long> bookIds = createCartBookListRequest.cartBookList().stream()
			.map(CreateCartBookRequest::bookId)
			.collect(Collectors.toList());

		// 책 목록 조회
		List<Book> books = bookRepository.findAllByIdIn(bookIds);
		Map<Long, Book> bookMap = books.stream().collect(Collectors.toMap(Book::getId, book -> book));

		// 기존 카트북 조회
		List<CartBook> existingCartBooks = cartBookRepository.findByCartIdAndBookIdIn(cart.getId(), bookIds);
		Map<Long, CartBook> existingCartBookMap = existingCartBooks.stream()
			.collect(Collectors.toMap(cartBook -> cartBook.getBook().getId(), cartBook -> cartBook));

		// 카트에 책 추가 또는 업데이트
		List<CartBook> cartBooksToSave = new ArrayList<>();
		for (CreateCartBookRequest bookRequest : createCartBookListRequest.cartBookList()) {
			Book book = bookMap.get(bookRequest.bookId());
			if (book == null) {
				throw new NotFoundException(ErrorCode.BOOK_NOT_FOUND);
			}

			CartBook cartBook = existingCartBookMap.get(bookRequest.bookId());
			if (cartBook != null) {
				// 기존 카트북 업데이트
				cartBook.updateQuantity(cartBook.getQuantity() + bookRequest.quantity());
				cartBooksToSave.add(cartBook);
			} else {
				// 새로운 카트북 생성
				CartBook newCartBook = new CartBook(cart, book, bookRequest.quantity());
				cartBooksToSave.add(newCartBook);
			}
		}

		// 카트북 저장
		cartBookRepository.saveAll(cartBooksToSave);

		// 응답 생성
		return new CreateCartBookListResponse(
			cartBooksToSave.stream().map(CartBook::getId).collect(Collectors.toList()));
	}

	@Override
	public void deleteCartBook(Long memberId, Long bookId) {
		Optional<Cart> cart = cartRepository.findByMemberId(memberId);
		if(cart.isPresent()) {
			Optional<CartBook> cartBook = cartBookRepository.findByCartIdAndBookIdAndIsExposed(cart.get().getId(), bookId, true);
			cartBook.ifPresent(book -> book.updateIsExposed(false));
		}
	}

	@Override
	public void deleteCartBooks(Long memberId, DeleteCartBookListRequest request) {
		// 회원 ID로 장바구니 조회
		Optional<Cart> optionalCart = cartRepository.findByMemberId(memberId);

		if (optionalCart.isPresent()) {
			Cart cart = optionalCart.get();

			// 요청된 도서 ID 리스트로 해당하는 CartBook 엔티티들 조회
			List<CartBook> cartBooksToDelete = cartBookRepository.findAllByCartAndBookIdIn(cart, request.bookIds());

			// 삭제 처리: 각 CartBook 엔티티의 isExposed를 false로 설정하고 저장
			cartBooksToDelete.forEach(cartBook -> cartBook.updateIsExposed(false));
			cartBookRepository.saveAll(cartBooksToDelete);
		}
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
