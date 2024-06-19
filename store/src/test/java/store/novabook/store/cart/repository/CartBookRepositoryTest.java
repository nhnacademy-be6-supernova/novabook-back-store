package store.novabook.store.cart.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import store.novabook.store.cart.service.CartBookService;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CartBookRepositoryTest {

	@Autowired
	private CartBookService cartBookService;

	@PersistenceContext
	private EntityManager entityManager;

	@Test
	void createCartBookTest() {

	}
}
