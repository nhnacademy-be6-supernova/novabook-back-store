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
import store.novabook.store.cart.dto.CartBookDTO;
import store.novabook.store.cart.dto.CartBookIdDTO;
import store.novabook.store.cart.dto.CartBookListDTO;
import store.novabook.store.cart.dto.request.DeleteCartBookListRequest;
import store.novabook.store.cart.dto.request.UpdateCartBookQuantityRequest;
import store.novabook.store.cart.dto.response.CreateCartBookListResponse;
import store.novabook.store.cart.dto.response.CreateCartBookResponse;
import store.novabook.store.cart.entity.Cart;
import store.novabook.store.cart.entity.CartBook;
import store.novabook.store.cart.repository.CartBookRepository;
import store.novabook.store.cart.repository.CartQueryRepository;
import store.novabook.store.cart.repository.CartRepository;
import store.novabook.store.cart.service.CartBookService;
import store.novabook.store.common.exception.BadRequestException;
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
	public CreateCartBookResponse createCartBook(Long memberId, CartBookDTO request) {
		if(memberId == null) {
			Book book = bookRepository.findById(request.bookId())
				.orElseThrow(() -> new NotFoundException(ErrorCode.BOOK_NOT_FOUND));

			if(book.getInventory() < request.quantity()){
				throw new BadRequestException(ErrorCode.NOT_UPDATE_CART_QUANTITY);
			}
			return new CreateCartBookResponse(book.getId());

		}

		CartBook cartBook = queryRepository.createCartBook(memberId, request);
		return new CreateCartBookResponse(cartBookRepository.save(cartBook).getId());
	}

	@Override
	public CreateCartBookListResponse createCartBooks(Long memberId,
		CartBookListDTO request) {

		// 카트 존재 여부 확인
		Cart cart = cartRepository.findByMemberId(memberId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.CART_NOT_FOUND));

		// 책 ID 목록 생성
		List<Long> bookIds = new ArrayList<>();
		for (CartBookDTO cartBook : request.getCartBookList()) {
			bookIds.add(cartBook.bookId());
		}

		// 책 목록 조회
		List<Book> books = bookRepository.findAllByIdIn(bookIds);
		Map<Long, Book> bookMap = books.stream().collect(Collectors.toMap(Book::getId, book -> book));

		// 기존 카트북 조회
		List<CartBook> existingCartBooks = cartBookRepository.findByCartIdAndBookIdInAndIsExposedTrue(cart.getId(), bookIds);
		Map<Long, CartBook> existingCartBookMap = existingCartBooks.stream()
			.collect(Collectors.toMap(cartBook -> cartBook.getBook().getId(), cartBook -> cartBook));

		// 카트에 책 추가 또는 업데이트
		List<CartBook> cartBooksToSave = new ArrayList<>();
		for (CartBookDTO cartBookDTO : request.getCartBookList()) {
			Long bookId = cartBookDTO.bookId();
			Book book = bookMap.get(bookId);
			if (book == null) {
				throw new NotFoundException(ErrorCode.BOOK_NOT_FOUND);
			}

			CartBook cartBook = existingCartBookMap.get(bookId);
			if (cartBook != null) {
				// 기존 카트북 업데이트
				int newQuantity = cartBook.getQuantity() + cartBookDTO.quantity();
				if (newQuantity > book.getInventory()) {
					newQuantity = book.getInventory();
				}
				cartBook.updateQuantity(newQuantity);
				cartBooksToSave.add(cartBook);
			} else {
				// 새로운 카트북 생성
				int initialQuantity = cartBookDTO.quantity();
				if (initialQuantity > book.getInventory()) {
					initialQuantity = book.getInventory();
				}
				CartBook newCartBook = new CartBook(cart, book, initialQuantity);
				cartBooksToSave.add(newCartBook);
			}
		}

		// 변경된 카트북 저장
		cartBookRepository.saveAll(cartBooksToSave);

		return new CreateCartBookListResponse(
			cartBooksToSave.stream().map(CartBook::getId).toList());
	}

	@Override
	public void deleteCartBook(Long memberId, Long bookId) {
		Optional<Cart> cart = cartRepository.findByMemberId(memberId);
		if (cart.isPresent()) {
			Optional<CartBook> cartBook = cartBookRepository.findByCartIdAndBookIdAndIsExposedTrue(cart.get().getId(),
				bookId);
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
			List<CartBook> cartBooksToDelete = cartBookRepository.findAllByCartAndBookIdInAndIsExposedTrue(cart, request.bookIds());

			// 삭제 처리: 각 CartBook 엔티티의 isExposed를 false로 설정하고 저장
			cartBooksToDelete.forEach(cartBook -> cartBook.updateIsExposed(false));
		}
	}

	@Override
	public void updateCartBookQuantity(Long memberId, UpdateCartBookQuantityRequest request) {
		// 회원 ID로 장바구니 조회
		Optional<Cart> optionalCart = cartRepository.findByMemberId(memberId);

		if (optionalCart.isPresent()) {
			Cart cart = optionalCart.get();

			// 요청된 도서 ID로 해당하는 CartBook 엔티티 조회
			CartBook cartBook = cartBookRepository.findByCartAndBookIdAndIsExposedTrue(cart, request.bookId());

			// Book의 inventory 및 상태 조회
			Book book = bookRepository.findById(request.bookId()).orElseThrow(() -> new NotFoundException(ErrorCode.BOOK_NOT_FOUND));

			// 요청된 수량이 inventory보다 작거나 같고, BookStatus가 1인 경우에만 업데이트
			if (request.quantity() <= book.getInventory() && book.getBookStatus().getId() == 1) {
				cartBook.updateQuantity(request.quantity());
			} else {
				throw new BadRequestException(ErrorCode.NOT_UPDATE_CART_QUANTITY);
			}
		}
	}

	@Override
	public CartBookListDTO getCartBookAllByGuest(CartBookIdDTO request) {
		return queryRepository.getCartBookAllGuest(request);
	}

	@Override
	public CartBookListDTO getCartBookAllByMemberId(Long memberId) {
		return queryRepository.getCartBookAllByMemberId(memberId);
	}



}
