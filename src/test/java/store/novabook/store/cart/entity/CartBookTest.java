package store.novabook.store.cart.entity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import store.novabook.store.book.entity.Book;

class CartBookTest {

	@Test
	void testCartBook() {

		Cart cartMock = mock(Cart.class);
		Book bookMock = mock(Book.class);

		CartBook cartBook = new CartBook(cartMock, bookMock, 1);

		assertEquals(cartMock, cartBook.getCart());
		assertEquals(bookMock, cartBook.getBook());
		assertEquals(1, cartBook.getQuantity());
	}
}