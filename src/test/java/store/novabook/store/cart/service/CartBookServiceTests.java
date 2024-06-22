package store.novabook.store.cart.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import store.novabook.store.book.entity.Book;
import store.novabook.store.book.repository.BookRepository;
import store.novabook.store.cart.dto.CreateCartBookRequest;
import store.novabook.store.cart.dto.DeleteCartBookRequest;
import store.novabook.store.cart.dto.GetCartBookResponse;
import store.novabook.store.cart.entity.Cart;
import store.novabook.store.cart.entity.CartBook;
import store.novabook.store.cart.repository.CartBookRepository;
import store.novabook.store.cart.repository.CartRepository;

public class CartBookServiceTests {

	@InjectMocks
	private CartBookService cartBookService;

	@Mock
	private CartBookRepository cartBookRepository;

	@Mock
	private CartRepository cartRepository;

	@Mock
	private BookRepository bookRepository;

	@Captor
	private ArgumentCaptor<Pageable> pageableCaptor;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void createCartBookTest() {

		CartBook cartBookMock = mock(CartBook.class);
		Cart cartMock = mock(Cart.class);
		Book bookMock = mock(Book.class);

		CreateCartBookRequest createCartBookRequest = CreateCartBookRequest.builder()
			.cartId(1L)
			.bookId(1L)
			.quantity(1)
			.build();

		when(cartBookRepository.findByCartIdAndBookId(
			createCartBookRequest.cartId(),
			createCartBookRequest.bookId())).thenReturn(java.util.Optional.of(cartBookMock));
		when(cartBookRepository.findByCartIdAndBookId(
			createCartBookRequest.cartId(),
			createCartBookRequest.bookId())).thenReturn(java.util.Optional.ofNullable(null));

		when(cartRepository.findById(createCartBookRequest.cartId())).thenReturn(java.util.Optional.of(cartMock));
		when(bookRepository.findById(createCartBookRequest.bookId())).thenReturn(java.util.Optional.of(bookMock));

		cartBookService.createCartBook(createCartBookRequest);

		verify(cartBookRepository, times(1)).save(any(CartBook.class));

	}

	@Test
	void getCartBookListByCartIdTest() {
		CartBook cartBookMock = mock(CartBook.class);
		Cart cartMock = mock(Cart.class);
		Book bookMock = mock(Book.class);

		when(cartBookMock.getCart()).thenReturn(cartMock);
		when(cartBookMock.getBook()).thenReturn(bookMock);
		when(cartMock.getId()).thenReturn(1L);
		when(bookMock.getId()).thenReturn(1L);

		List<CartBook> cartBookList = Collections.singletonList(cartBookMock);
		Page<CartBook> page = new PageImpl<>(cartBookList, PageRequest.of(0, 10), cartBookList.size());

		when(cartBookRepository.findAllByCartId(eq(cartMock.getId()), pageableCaptor.capture())).thenReturn(
			Optional.of(page));

		Page<GetCartBookResponse> result = cartBookService.getCartBookListByCartId(cartMock.getId(),
			PageRequest.of(0, 10));

		assertAll(
			() -> assertNotNull(result),
			() -> assertEquals(1, result.getTotalElements()),
			() -> assertEquals(0, pageableCaptor.getValue().getPageNumber()),
			() -> assertEquals(10, pageableCaptor.getValue().getPageSize())
		);
	}

	@Test
	void deleteCartBookAndBookTest() {
		CartBook cartBookMock = mock(CartBook.class);

		DeleteCartBookRequest deleteCartBookRequest = DeleteCartBookRequest.from(1L, 1L);
		when(cartBookRepository.existsByCartIdAndBookId(1L, 1L)).thenReturn(true);

		cartBookService.deleteCartBookAndBook(deleteCartBookRequest);

		verify(cartBookRepository, times(1)).deleteByCartIdAndBookId(1L, 1L);
	}
}
