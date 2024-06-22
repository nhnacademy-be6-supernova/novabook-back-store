package store.novabook.store.cart.entity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import store.novabook.store.user.member.entity.Users;

public class CartTest {

	@Test
	void testCart() {
		Users usersMock = mock(Users.class);

		Cart cart = new Cart(null, usersMock, true, LocalDateTime.now(), null);

		assertEquals(usersMock, cart.getUsers());
		assertEquals(true, cart.getIsExposed());
		assertTrue(cart.getCreatedAt().isBefore(LocalDateTime.now()));
	}
}