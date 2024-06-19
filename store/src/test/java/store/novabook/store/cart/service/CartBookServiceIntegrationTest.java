package store.novabook.store.cart.service;

public class CartBookServiceIntegrationTest {

	// @Autowired
	// private TestEntityManager entityManager;
	//
	// @Autowired
	// private CartBookService cartBookService;
	//
	// @Test
	// public void createCartBookTest() {
	// 	// Given
	// 	Cart cart = Cart.builder()
	// 		.id(1L)
	// 		.users(null)
	// 		.isExposed(false)
	// 		.build();
	//
	// 	entityManager.persist(cart);
	//
	// 	CreateCartBookRequest createCartBookRequest = CreateCartBookRequest.builder()
	// 		.cartId(cart.getId())
	// 		.bookId(1L)
	// 		.quantity(1)
	// 		.build();
	//
	// 	// When
	// 	cartBookService.createCartBook(createCartBookRequest);
	//
	// 	// Then
	// 	CartBook cartBook = entityManager.find(CartBook.class, createCartBookRequest.cartId());
	// 	assertNotNull(cartBook);
	// 	assertEquals(createCartBookRequest.quantity(), cartBook.getQuantity());
	// }
}
