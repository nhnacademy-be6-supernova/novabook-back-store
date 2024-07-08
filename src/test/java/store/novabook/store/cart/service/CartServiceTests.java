// package store.novabook.store.cart.service;
//
// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.*;
// import static org.mockito.Mockito.anyLong;
// import static org.mockito.Mockito.*;
//
// import java.util.Optional;
//
// import org.junit.jupiter.api.AfterEach;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
//
// import store.novabook.store.cart.dto.CreateCartRequest;
// import store.novabook.store.cart.dto.CartBookListDTO;
// import store.novabook.store.cart.entity.Cart;
// import store.novabook.store.cart.repository.CartRepository;
// import store.novabook.store.common.exception.EntityNotFoundException;
//
// public class CartServiceTests {
//
// 	@InjectMocks
// 	private CartService cartService;
//
// 	@Mock
// 	private CartRepository cartRepository;
//
// 	@Mock
// 	private UsersRepository usersRepository;
//
// 	@BeforeEach
// 	void setUp() {
// 		MockitoAnnotations.openMocks(this);
// 	}
//
// 	@AfterEach
// 	void tearDown() {
// 		try {
// 			MockitoAnnotations.openMocks(this).close();
// 		} catch (Exception e) {
// 			throw new RuntimeException(e);
// 		}
// 	}
//
// 	@Test
// 	void createCartTest() {
// 		Users usersMock = mock(Users.class);
// 		Cart cartMock = mock(Cart.class);
//
// 		when(usersRepository.findById(usersMock.getId())).thenReturn(java.util.Optional.of(usersMock));
//
// 		CreateCartRequest createCartRequest = CreateCartRequest.builder()
// 			.ordersBookId(usersMock.getId())
// 			.isExposed(false)
// 			.build();
//
// 		when(cartRepository.findById(createCartRequest.ordersBookId())).thenReturn(java.util.Optional.of(cartMock));
//
// 		cartService.createCart(createCartRequest);
//
// 		verify(cartRepository, times(1)).save(any(Cart.class));
// 	}
//
// 	@Test
// 	void getCartByMemberIdTest() {
// 		Users usersMock = mock(Users.class);
// 		Cart cartMock = mock(Cart.class);
// 		when(cartMock.getUsers()).thenReturn(usersMock);
// 		when(cartMock.getIsExposed()).thenReturn(true);
// 		when(cartRepository.findByUsersId(anyLong())).thenReturn(Optional.of(cartMock));
//
// 		CartBookListDTO result = cartService.getCartByMemberId(1L);
//
// 		assertNotNull(result);
// 		assertEquals(usersMock, result.users());
// 		assertTrue(result.isExposed());
// 	}
//
// 	@Test
// 	void getCartByMemberIdTest_EntityNotFoundException() {
// 		when(cartRepository.findByUsersId(anyLong())).thenReturn(Optional.empty());
//
// 		assertThrows(EntityNotFoundException.class, () -> {
// 			cartService.getCartByMemberId(1L);
// 		});
// 	}
//
// }
