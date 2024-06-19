package store.novabook.store.cart.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import store.novabook.store.book.entity.Book;
import store.novabook.store.book.repository.BookRepository;
import store.novabook.store.cart.dto.CreateCartBookRequest;
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

		// when(cartBookRepository.findByCart_IdAndBook_Id(
		// 	createCartBookRequest.cartId(),
		// 	createCartBookRequest.bookId())).thenReturn(java.util.Optional.of(cartBookMock));
		when(cartBookRepository.findByCartIdAndBook_Id(
			createCartBookRequest.cartId(),
			createCartBookRequest.bookId())).thenReturn(java.util.Optional.ofNullable(null));

		when(cartRepository.findById(createCartBookRequest.cartId())).thenReturn(java.util.Optional.of(cartMock));
		when(bookRepository.findById(createCartBookRequest.bookId())).thenReturn(java.util.Optional.of(bookMock));

		cartBookService.createCartBook(createCartBookRequest);

		verify(cartBookRepository, times(1)).save(any(CartBook.class));

	}
}
