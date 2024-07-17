// package store.novabook.store.cart.service.impl;
//
// import static org.assertj.core.api.Assertions.*;
// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;
//
// import java.time.LocalDateTime;
// import java.util.Collections;
// import java.util.List;
// import java.util.Map;
// import java.util.Optional;
//
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
//
// import store.novabook.store.book.entity.Book;
// import store.novabook.store.book.entity.BookStatus;
// import store.novabook.store.book.repository.BookRepository;
// import store.novabook.store.cart.dto.CartBookDTO;
// import store.novabook.store.cart.dto.CartBookIdDTO;
// import store.novabook.store.cart.dto.CartBookListDTO;
// import store.novabook.store.cart.dto.request.DeleteCartBookListRequest;
// import store.novabook.store.cart.dto.request.UpdateCartBookQuantityRequest;
// import store.novabook.store.cart.dto.response.CreateCartBookListResponse;
// import store.novabook.store.cart.dto.response.CreateCartBookResponse;
// import store.novabook.store.cart.entity.Cart;
// import store.novabook.store.cart.entity.CartBook;
// import store.novabook.store.cart.repository.CartBookRepository;
// import store.novabook.store.cart.repository.CartQueryRepository;
// import store.novabook.store.cart.repository.CartRepository;
// import store.novabook.store.member.entity.Member;
// import store.novabook.store.member.entity.MemberStatus;
//
// @ExtendWith(MockitoExtension.class)
// public class CartBookServiceImplTest {
//
// 	@Mock
// 	private CartRepository cartRepository;
//
// 	@Mock
// 	private CartBookRepository cartBookRepository;
//
// 	@Mock
// 	private BookRepository bookRepository;
//
// 	@Mock
// 	private CartQueryRepository queryRepository;
//
// 	@InjectMocks
// 	private CartBookServiceImpl cartBookService;
//
// 	private Book book;
// 	private Cart cart;
// 	private CartBook cartBook;
// 	private CartBookDTO cartBookDTO;
//
// 	@BeforeEach
// 	void setUp() {
//
// 		MemberStatus memberStatus = MemberStatus.builder().build();
// 		BookStatus bookStatus = BookStatus.builder()
// 			.name("Available")
// 			.build();
// 		Member member = new Member("testLoginId", "encodedPassword", "testName", "testNumber",
// 			"testEmail", LocalDateTime.of(1990, 1, 1, 0, 0),
// 			LocalDateTime.now(), 1, memberStatus, "ROLE_MEMBERS");
// 		cart = Cart.builder()
// 			.member(member)
// 			.build();
//
//
// 		book = Book.builder()
// 			.bookStatus(bookStatus)
// 			.isbn("1234567890")
// 			.title("Test Book")
// 			.description("Test Description")
// 			.descriptionDetail("Detailed Description")
// 			.author("Author")
// 			.publisher("Publisher")
// 			.publicationDate(LocalDateTime.now())
// 			.inventory(10)
// 			.price(1000L)
// 			.discountPrice(900L)
// 			.isPackaged(false)
// 			.build();
//
// 		cartBook = CartBook.builder()
// 			.cart(cart)
// 			.book(book)
// 			.quantity(2)
// 			.build();
//
// 		cartBookDTO = CartBookDTO.builder()
// 			.bookId(book.getId())
// 			.quantity(1)
// 			.price(book.getPrice())
// 			.discountPrice(book.getDiscountPrice())
// 			.build();
// 	}
//
// 	@Test
// 	void createCartBook_withMemberId() {
// 		when(queryRepository.createCartBook(anyLong(), any(CartBookDTO.class))).thenReturn(cartBook);
// 		when(cartBookRepository.save(any(CartBook.class))).thenReturn(cartBook);
//
// 		CreateCartBookResponse response = cartBookService.createCartBook(1L, cartBookDTO);
//
// 		assertThat(response.id()).isEqualTo(cartBook.getId());
// 		verify(cartBookRepository).save(cartBook);
// 	}
//
// 	@Test
// 	void createCartBook_withoutMemberId() {
// 		CreateCartBookResponse response = new CreateCartBookResponse(book.getId());
//
// 		when(bookRepository.findById(anyLong())).thenReturn(Optional.ofNullable(book));
//
//
// 		assertThat(response.id()).isEqualTo(book.getId());
// 		verify(bookRepository).findById(book.getId());
// 	}
//
// 	@Test
// 	void createCartBooks() {
// 		when(cartRepository.findByMemberId(anyLong())).thenReturn(Optional.of(cart));
// 		when(bookRepository.findAllByIdIn(anyList())).thenReturn(List.of(book));
// 		when(cartBookRepository.findByCartIdAndBookIdInAndIsExposedTrue(anyLong(), anyList()))
// 			.thenReturn(Collections.emptyList());
// 		when(cartBookRepository.saveAll(anyList())).thenReturn(List.of(cartBook));
//
// 		CreateCartBookListResponse response = cartBookService.createCartBooks(1L, new CartBookListDTO(List.of(cartBookDTO)));
//
// 		assertThat(response.ids()).containsExactly(cartBook.getId());
// 		verify(cartBookRepository).saveAll(anyList());
// 	}
//
// 	@Test
// 	void deleteCartBook() {
// 		when(cartRepository.findByMemberId(anyLong())).thenReturn(Optional.of(cart));
// 		when(cartBookRepository.findByCartIdAndBookIdAndIsExposed(anyLong(), anyLong(), anyBoolean()))
// 			.thenReturn(Optional.of(cartBook));
//
// 		cartBookService.deleteCartBook(1L, book.getId());
//
// 		verify(cartBookRepository).findByCartIdAndBookIdAndIsExposed(cart.getId(), book.getId(), true);
// 		assertThat(cartBook.isExposed()).isFalse();
// 	}
//
// 	@Test
// 	void deleteCartBooks() {
// 		when(cartRepository.findByMemberId(anyLong())).thenReturn(Optional.of(cart));
// 		when(cartBookRepository.findAllByCartAndBookIdInAndIsExposedTrue(any(Cart.class), anyList()))
// 			.thenReturn(List.of(cartBook));
//
// 		cartBookService.deleteCartBooks(1L, new DeleteCartBookListRequest(List.of(book.getId())));
//
// 		verify(cartBookRepository).findAllByCartAndBookIdInAndIsExposedTrue(cart, List.of(book.getId()));
// 		assertThat(cartBook.isExposed()).isFalse();
// 	}
//
// 	@Test
// 	void updateCartBookQuantity() {
// 		when(cartRepository.findByMemberId(anyLong())).thenReturn(Optional.of(cart));
// 		when(cartBookRepository.findByCartAndBookIdAndIsExposedTrue(any(Cart.class), anyLong())).thenReturn(cartBook);
// 		when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
//
// 		UpdateCartBookQuantityRequest request = new UpdateCartBookQuantityRequest(book.getId(), 5);
// 		cartBookService.updateCartBookQuantity(1L, request);
//
// 		assertThat(cartBook.getQuantity()).isEqualTo(5);
// 	}
//
// 	@Test
// 	void getCartBookAllByGuest() {
// 		CartBookIdDTO request = new CartBookIdDTO(Map.of(book.getId(), 1));
// 		when(queryRepository.getCartBookAllGuest(any(CartBookIdDTO.class))).thenReturn(new CartBookListDTO(List.of(cartBookDTO)));
//
// 		CartBookListDTO response = cartBookService.getCartBookAllByGuest(request);
//
// 		assertThat(response.getCartBookList()).containsExactly(cartBookDTO);
// 	}
//
// 	@Test
// 	void getCartBookAllByMemberId() {
// 		when(queryRepository.getCartBookAllByMemberId(anyLong())).thenReturn(new CartBookListDTO(List.of(cartBookDTO)));
//
// 		CartBookListDTO response = cartBookService.getCartBookAllByMemberId(1L);
//
// 		assertThat(response.getCartBookList()).containsExactly(cartBookDTO);
// 	}
// }