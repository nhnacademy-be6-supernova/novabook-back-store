package store.novabook.store.cart.service;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;

import store.novabook.store.book.entity.Book;
import store.novabook.store.book.repository.BookRepository;
import store.novabook.store.cart.entity.Cart;
import store.novabook.store.cart.entity.CartBook;
import store.novabook.store.cart.repository.CartBookRepository;
import store.novabook.store.cart.repository.CartRepository;
import store.novabook.store.cart.service.impl.CartBookServiceImpl;

public class CartBookServiceImplImplTests {

	@InjectMocks
	private CartBookServiceImpl cartBookServiceImpl;

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

		cartBookServiceImpl.createCartBook(createCartBookRequest);

		verify(cartBookRepository, times(1)).save(any(CartBook.class));

	}

	@Test
	void deleteCartBookAndBookTest() {
		CartBook cartBookMock = mock(CartBook.class);

		DeleteCartBookRequest deleteCartBookRequest = DeleteCartBookRequest.from(1L, 1L);
		when(cartBookRepository.existsByCartIdAndBookId(1L, 1L)).thenReturn(true);

		cartBookServiceImpl.deleteCartBookAndBook(deleteCartBookRequest);

		verify(cartBookRepository, times(1)).deleteByCartIdAndBookId(1L, 1L);
	}
}
