package store.novabook.store.orders.service.impl;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import store.novabook.store.book.dto.response.GetOrdersBookReviewIdResponse;
import store.novabook.store.book.entity.Book;
import store.novabook.store.book.repository.BookRepository;
import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.common.exception.NotFoundException;
import store.novabook.store.orders.dto.request.CreateDeliveryFeeRequest;
import store.novabook.store.orders.dto.request.CreateOrdersBookRequest;
import store.novabook.store.orders.dto.request.CreateOrdersRequest;
import store.novabook.store.orders.dto.request.CreateOrdersStatusRequest;
import store.novabook.store.orders.dto.request.CreateWrappingPaperRequest;
import store.novabook.store.orders.dto.response.CreateResponse;
import store.novabook.store.orders.dto.response.GetOrderDetailResponse;
import store.novabook.store.orders.dto.response.GetOrdersBookResponse;
import store.novabook.store.orders.entity.DeliveryFee;
import store.novabook.store.orders.entity.Orders;
import store.novabook.store.orders.entity.OrdersBook;
import store.novabook.store.orders.entity.OrdersStatus;
import store.novabook.store.orders.entity.WrappingPaper;
import store.novabook.store.orders.repository.OrdersBookRepository;
import store.novabook.store.orders.repository.OrdersRepository;

@ExtendWith(MockitoExtension.class)
class OrdersBookServiceImplTest {

	@InjectMocks
	private OrdersBookServiceImpl ordersBookService;

	@Mock
	private OrdersBookRepository ordersBookRepository;

	@Mock
	private OrdersRepository ordersRepository;

	@Mock
	private BookRepository bookRepository;

	private Orders orders;
	private Book book;
	private OrdersBook ordersBook;
	private OrdersStatus ordersStatus;
	private DeliveryFee deliveryFee;
	private WrappingPaper wrappingPaper;

	@BeforeEach
	void setUp() {
		ordersStatus = OrdersStatus.builder()
			.request(new CreateOrdersStatusRequest("Pending"))
			.build();

		deliveryFee = DeliveryFee.builder()
			.createDeliveryFeeRequest(new CreateDeliveryFeeRequest(500L))
			.build();

		wrappingPaper = WrappingPaper.builder()
			.request(new CreateWrappingPaperRequest(300L, "Standard", "Available"))
			.build();

		orders = Orders.builder()
			.member(null) // 필요한 경우 Member 객체로 초기화
			.deliveryFee(deliveryFee)
			.wrappingPaper(wrappingPaper)
			.ordersStatus(ordersStatus)
			.payment(null) // 필요한 경우 Payment 객체로 초기화
			.request(
				new CreateOrdersRequest(1L, 1L, 1L, 1L, null, LocalDateTime.now(), "code", 1000L, LocalDateTime.now(),
					500L, "address", "sender", "010-1234-5678", "receiver", "010-9876-5432", 100L, 50L, null, 0L))
			.build();

		book = Book.builder()
			.bookStatus(null) // 필요한 경우 BookStatus 객체로 초기화
			.isbn("isbn")
			.title("title")
			.description("description")
			.descriptionDetail("descriptionDetail")
			.author("author")
			.publisher("publisher")
			.publicationDate(LocalDateTime.now())
			.inventory(10)
			.price(1000L)
			.discountPrice(800L)
			.isPackaged(false)
			.build();

		ordersBook = new OrdersBook(orders, book,
			new CreateOrdersBookRequest(1L, 1L, 1, 1000L)); // 필요에 따라 OrdersBook 객체 초기화
	}

	@Test
	void create_OrdersAndBookFound_ReturnsCreateResponse() {
		when(ordersRepository.findById(anyLong())).thenReturn(Optional.of(orders));
		when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
		when(ordersBookRepository.save(any(OrdersBook.class))).thenReturn(ordersBook);

		CreateOrdersBookRequest request = new CreateOrdersBookRequest(1L, 1L, 1, 1000L);
		CreateResponse response = ordersBookService.create(request);

		assertThat(response).isNotNull();
		assertThat(response.id()).isEqualTo(ordersBook.getId());
		verify(ordersRepository).findById(request.ordersId());
		verify(bookRepository).findById(request.bookId());
		verify(ordersBookRepository).save(any(OrdersBook.class));
	}

	@Test
	void create_OrdersNotFound_ThrowsNotFoundException() {
		when(ordersRepository.findById(anyLong())).thenReturn(Optional.empty());

		CreateOrdersBookRequest request = new CreateOrdersBookRequest(1L, 1L, 1, 1000L);

		assertThatThrownBy(() -> ordersBookService.create(request))
			.isInstanceOf(NotFoundException.class)
			.hasMessage(ErrorCode.ORDERS_NOT_FOUND.getMessage());

		verify(ordersRepository).findById(request.ordersId());
		verify(bookRepository, never()).findById(anyLong());
		verify(ordersBookRepository, never()).save(any(OrdersBook.class));
	}

	@Test
	void create_BookNotFound_ThrowsNotFoundException() {
		when(ordersRepository.findById(anyLong())).thenReturn(Optional.of(orders));
		when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

		CreateOrdersBookRequest request = new CreateOrdersBookRequest(1L, 1L, 1, 1000L);

		assertThatThrownBy(() -> ordersBookService.create(request))
			.isInstanceOf(NotFoundException.class)
			.hasMessage(ErrorCode.BOOK_NOT_FOUND.getMessage());

		verify(ordersRepository).findById(request.ordersId());
		verify(bookRepository).findById(request.bookId());
		verify(ordersBookRepository, never()).save(any(OrdersBook.class));
	}

	@Test
	void delete_ExistingId_DeletesOrdersBook() {
		Long id = 1L;
		doNothing().when(ordersBookRepository).deleteById(id);

		ordersBookService.delete(id);

		verify(ordersBookRepository).deleteById(id);
	}

	@Test
	void getOrdersBookReviewByMemberId_ValidMemberId_ReturnsPage() {
		Long memberId = 1L;
		Pageable pageable = PageRequest.of(0, 10);
		List<GetOrdersBookReviewIdResponse> responses = List.of(
			new GetOrdersBookReviewIdResponse(1L, 1L, 1L, 1L, "Test Book", LocalDateTime.now()));
		Page<GetOrdersBookReviewIdResponse> page = new PageImpl<>(responses);
		when(ordersBookRepository.getOrdersBookReviewIdByMemberId(memberId, pageable)).thenReturn(page);

		Page<GetOrdersBookReviewIdResponse> result = ordersBookService.getOrdersBookReviewByMemberId(memberId,
			pageable);

		assertThat(result).isEqualTo(page);
		verify(ordersBookRepository).getOrdersBookReviewIdByMemberId(memberId, pageable);
	}

	@Test
	void getOrdersBookByMemberId_ValidMemberId_ReturnsPage() {
		Long memberId = 1L;
		Pageable pageable = PageRequest.of(0, 10);
		List<GetOrdersBookResponse> responses = List.of(
			new GetOrdersBookResponse(1L, "title", 1L, 1000L, "status", LocalDateTime.now()));
		Page<GetOrdersBookResponse> page = new PageImpl<>(responses);
		when(ordersBookRepository.getOrdersBookByMemberId(memberId, pageable)).thenReturn(page);

		Page<GetOrdersBookResponse> result = ordersBookService.getOrdersBookByMemberId(memberId, pageable);

		assertThat(result).isEqualTo(page);
		verify(ordersBookRepository).getOrdersBookByMemberId(memberId, pageable);
	}

	@Test
	void getOrderDetail_ValidOrdersId_ReturnsGetOrderDetailResponse() {
		Long ordersId = 1L;
		List<OrdersBook> ordersBooks = List.of(ordersBook);
		when(ordersBookRepository.getOrderDetailByOrdersId(ordersId)).thenReturn(ordersBooks);

		GetOrderDetailResponse response = ordersBookService.getOrderDetail(ordersId);

		assertThat(response).isNotNull();
		assertThat(response.ordersStatusName()).isEqualTo("Pending");
		verify(ordersBookRepository).getOrderDetailByOrdersId(ordersId);
	}
}
