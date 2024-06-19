package store.novabook.store.cart.repository;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CartBookRepositoryTest {

	// @Autowired
	// private CartBookService cartBookService;
	//
	// @PersistenceContext
	// private EntityManager entityManager;
	//
	// @Test
	// void createCartBookTest() {
	//
	// }
}
