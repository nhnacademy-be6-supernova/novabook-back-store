package store.novabook.store.cart.entity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import store.novabook.store.book.entity.Book;

public class CartBookTest {

	@Test
	void testCartBook() {

		Cart cartMock = mock(Cart.class);
		Book bookMock = mock(Book.class);

		CartBook cartBook = new CartBook(null, cartMock, bookMock, 1, LocalDateTime.now(), null);

		assertEquals(cartMock, cartBook.getCart());
		assertEquals(bookMock, cartBook.getBook());
		assertEquals(1, cartBook.getQuantity());
		assertTrue(cartBook.getCreatedAt().isBefore(LocalDateTime.now()));
	}
}