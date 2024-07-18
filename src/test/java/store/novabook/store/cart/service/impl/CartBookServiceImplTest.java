package store.novabook.store.cart.service.impl;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import store.novabook.store.book.entity.Book;
import store.novabook.store.book.entity.BookStatus;
import store.novabook.store.book.repository.BookRepository;
import store.novabook.store.cart.dto.CartBookDTO;
import store.novabook.store.cart.dto.CartBookIdDTO;
import store.novabook.store.cart.dto.CartBookListDTO;
import store.novabook.store.cart.dto.request.DeleteCartBookListRequest;
import store.novabook.store.cart.dto.response.CreateCartBookListResponse;
import store.novabook.store.cart.dto.response.CreateCartBookResponse;
import store.novabook.store.cart.entity.Cart;
import store.novabook.store.cart.entity.CartBook;
import store.novabook.store.cart.repository.CartBookRepository;
import store.novabook.store.cart.repository.CartQueryRepository;
import store.novabook.store.cart.repository.CartRepository;
import store.novabook.store.common.exception.NotFoundException;
import store.novabook.store.member.entity.Member;
import store.novabook.store.member.entity.MemberStatus;

@ExtendWith(MockitoExtension.class)
public class CartBookServiceImplTest {

	@Mock
	private CartRepository cartRepository;

	@Mock
	private CartBookRepository cartBookRepository;

	@Mock
	private BookRepository bookRepository;

	@Mock
	private CartQueryRepository queryRepository;

	@InjectMocks
	private CartBookServiceImpl cartBookService;

	private Book book;
	private Cart cart;
	private CartBook cartBook;
	private CartBookDTO cartBookDTO;

	@BeforeEach
	void setUp() {

		MemberStatus memberStatus = MemberStatus.builder().build();
		BookStatus bookStatus = BookStatus.builder()
			.name("Available")
			.build();
		Member member = new Member("testLoginId", "encodedPassword", "testName", "testNumber",
			"testEmail", LocalDateTime.of(1990, 1, 1, 0, 0),
			LocalDateTime.now(), 1, memberStatus, "ROLE_MEMBERS");
		cart = Cart.builder()
			.member(member)
			.build();


		book = Book.builder()
			.bookStatus(bookStatus)
			.isbn("1234567890")
			.title("Test Book")
			.description("Test Description")
			.descriptionDetail("Detailed Description")
			.author("Author")
			.publisher("Publisher")
			.publicationDate(LocalDateTime.now())
			.inventory(10)
			.price(1000L)
			.discountPrice(900L)
			.isPackaged(false)
			.build();

		cartBook = CartBook.builder()
			.cart(cart)
			.book(book)
			.quantity(2)
			.build();

		cartBookDTO = CartBookDTO.builder()
			.bookId(book.getId())
			.quantity(1)
			.price(book.getPrice())
			.discountPrice(book.getDiscountPrice())
			.build();
	}

	@Test
	void createCartBook_withMemberId() {
		when(queryRepository.createCartBook(anyLong(), any(CartBookDTO.class))).thenReturn(cartBook);
		when(cartBookRepository.save(any(CartBook.class))).thenReturn(cartBook);

		CreateCartBookResponse response = cartBookService.createCartBook(1L, cartBookDTO);

		assertThat(response.id()).isEqualTo(cartBook.getId());
		verify(cartBookRepository).save(cartBook);
	}

	@Test
	void createCartBook_withoutMemberId() {
		// Given
		Long memberId = 999L;

		// Mocking cartRepository.findByMemberId
		when(cartRepository.findByMemberId(memberId)).thenReturn(Optional.empty());

		// Prepare request DTO
		List<CartBookDTO> cartBookDTOList = List.of(cartBookDTO);
		CartBookListDTO request = new CartBookListDTO(cartBookDTOList);

		// When, Then
		assertThrows(NotFoundException.class, () -> cartBookService.createCartBooks(memberId, request));

		// Verify method invocations
		verify(cartRepository).findByMemberId(memberId);
		verifyNoMoreInteractions(bookRepository, cartBookRepository);
	}

	@Test
	void createCartBooks_whenInventoryExceeded() {
		// Given
		Long memberId = 1L;
		List<CartBookDTO> cartBookDTOs = Collections.singletonList(cartBookDTO);

		// Stubbing cartRepository.findByMemberId
		when(cartRepository.findByMemberId(memberId)).thenReturn(Optional.of(cart));

		// Stubbing bookRepository.findAllByIdIn
		when(bookRepository.findAllByIdIn(anyList())).thenReturn(Collections.singletonList(book));

		// Stubbing cartBookRepository.findByCartIdAndBookIdInAndIsExposedTrue
		when(cartBookRepository.findByCartIdAndBookIdInAndIsExposedTrue(eq(cart.getId()), anyList()))
			.thenReturn(Collections.emptyList());

		// Stubbing cartBookRepository.saveAll
		when(cartBookRepository.saveAll(anyList())).thenReturn(Collections.singletonList(cartBook));

		CartBookListDTO request = new CartBookListDTO(cartBookDTOs);

		// When
		CreateCartBookListResponse response = cartBookService.createCartBooks(memberId, request);

		// Then
		assertNotNull(response);
		assertEquals(1, response.ids().size());

		// Verify interactions
		verify(cartRepository).findByMemberId(memberId);
		verify(bookRepository).findAllByIdIn(anyList());
		verify(cartBookRepository).findByCartIdAndBookIdInAndIsExposedTrue(eq(cart.getId()), anyList());
		verify(cartBookRepository).saveAll(anyList());
	}

	@Test
	void createCartBooks_whenBookNotFound() {
		// Given
		Long memberId = 1L;
		List<CartBookDTO> cartBookDTOs = Collections.singletonList(cartBookDTO);

		// Stubbing cartRepository.findByMemberId
		when(cartRepository.findByMemberId(memberId)).thenReturn(Optional.of(cart));

		// Stubbing bookRepository.findAllByIdIn
		when(bookRepository.findAllByIdIn(anyList())).thenReturn(Collections.emptyList()); // Simulate book not found

		CartBookListDTO request = new CartBookListDTO(cartBookDTOs);

		// When, Then
		assertThrows(NotFoundException.class, () -> cartBookService.createCartBooks(memberId,
			request));

		// Verify interactions
		verify(cartRepository).findByMemberId(memberId);
		verify(bookRepository).findAllByIdIn(anyList());
		verify(cartBookRepository, never()).findByCartIdAndBookIdInAndIsExposedTrue(anyLong(), anyList());
		verify(cartBookRepository, never()).saveAll(anyList());
	}

	@Test
	void deleteCartBook() {
		// Given
		Long memberId = 1L;
		Long bookId = book.getId();

		// Stubbing cartRepository.findByMemberId
		when(cartRepository.findByMemberId(memberId)).thenReturn(Optional.of(cart));

		// Stubbing cartBookRepository.findByCartIdAndBookIdAndIsExposed
		when(cartBookRepository.findByCartIdAndBookIdAndIsExposed(eq(cart.getId()), eq(bookId), eq(true)))
			.thenReturn(Optional.of(cartBook));

		// When
		cartBookService.deleteCartBook(memberId, bookId);

		// Then
		verify(cartBookRepository).findByCartIdAndBookIdAndIsExposed(eq(cart.getId()), eq(bookId), eq(true));
		assertFalse(cartBook.isExposed());
	}

	@Test
	void deleteCartBooks() {
		// Given
		Long memberId = 1L;
		List<Long> bookIds = Collections.singletonList(book.getId());
		DeleteCartBookListRequest request = new DeleteCartBookListRequest(bookIds);

		// Stubbing cartRepository.findByMemberId
		when(cartRepository.findByMemberId(memberId)).thenReturn(Optional.of(cart));

		// Stubbing cartBookRepository.findAllByCartAndBookIdInAndIsExposedTrue
		when(cartBookRepository.findAllByCartAndBookIdInAndIsExposedTrue(eq(cart), anyList()))
			.thenReturn(Collections.singletonList(cartBook));

		// When
		cartBookService.deleteCartBooks(memberId, request);

		// Then
		verify(cartBookRepository).findAllByCartAndBookIdInAndIsExposedTrue(eq(cart), anyList());
		assertFalse(cartBook.isExposed());
	}

	@Test
	void updateCartBookQuantity() {

	}

	@Test
	void getCartBookAllByGuest() {
		Map<Long, Integer> bookIdsAndQuantity = new HashMap<>();
		bookIdsAndQuantity.put(1L, 1);
		CartBookIdDTO request = new CartBookIdDTO(bookIdsAndQuantity);
		when(queryRepository.getCartBookAllGuest(any(CartBookIdDTO.class))).thenReturn(new CartBookListDTO(List.of(cartBookDTO)));

		CartBookListDTO response = cartBookService.getCartBookAllByGuest(request);

		assertThat(response.getCartBookList()).containsExactly(cartBookDTO);
	}

	@Test
	void getCartBookAllByMemberId() {
		when(queryRepository.getCartBookAllByMemberId(anyLong())).thenReturn(new CartBookListDTO(List.of(cartBookDTO)));

		CartBookListDTO response = cartBookService.getCartBookAllByMemberId(1L);

		assertThat(response.getCartBookList()).containsExactly(cartBookDTO);
	}
}