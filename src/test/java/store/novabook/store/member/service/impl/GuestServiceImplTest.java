package store.novabook.store.member.service.impl;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import store.novabook.store.book.entity.Book;
import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.common.exception.NotFoundException;
import store.novabook.store.orders.dto.request.CreateDeliveryFeeRequest;
import store.novabook.store.orders.dto.request.CreateOrdersRequest;
import store.novabook.store.orders.dto.request.CreateOrdersStatusRequest;
import store.novabook.store.orders.dto.request.CreateWrappingPaperRequest;
import store.novabook.store.orders.dto.request.GetGuestOrderHistoryRequest;
import store.novabook.store.orders.dto.response.GetOrderDetailResponse;
import store.novabook.store.orders.entity.DeliveryFee;
import store.novabook.store.orders.entity.Orders;
import store.novabook.store.orders.entity.OrdersBook;
import store.novabook.store.orders.entity.OrdersStatus;
import store.novabook.store.orders.entity.WrappingPaper;
import store.novabook.store.orders.repository.OrdersBookRepository;

@ExtendWith(MockitoExtension.class)
class GuestServiceImplTest {

	@InjectMocks
	private GuestServiceImpl guestService;

	@Mock
	private OrdersBookRepository ordersBookRepository;

	private GetGuestOrderHistoryRequest validRequest;
	private GetGuestOrderHistoryRequest invalidRequest;

	@BeforeEach
	void setUp() {
		validRequest = new GetGuestOrderHistoryRequest("valid_code", "010-1234-5678");
		invalidRequest = new GetGuestOrderHistoryRequest("invalid_code", "010-1234-5678");
	}

	@Test
	void testGetOrderGuestSuccess() {
		Orders orders = createOrders();
		OrdersBook ordersBook = createOrdersBook(orders);

		given(ordersBookRepository.findByOrdersCode(anyString())).willReturn(List.of(ordersBook));

		GetOrderDetailResponse response = guestService.getOrderGuest(validRequest);

		assertThat(response).isNotNull();
		assertThat(response.ordersId()).isEqualTo(orders.getId());
	}

	@Test
	void testGetOrderGuestNotFound() {
		given(ordersBookRepository.findByOrdersCode(anyString())).willReturn(Collections.emptyList());

		NotFoundException exception = assertThrows(NotFoundException.class, () -> {
			guestService.getOrderGuest(invalidRequest);
		});

		assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.ORDERS_NOT_FOUND);
	}

	private Orders createOrders() {
		OrdersStatus status = OrdersStatus.builder().request(new CreateOrdersStatusRequest("Order Placed")).build();

		DeliveryFee deliveryFee = DeliveryFee.builder()
			.createDeliveryFeeRequest(new CreateDeliveryFeeRequest(1000L))
			.build();

		WrappingPaper wrappingPaper = WrappingPaper.builder()
			.request(new CreateWrappingPaperRequest(1000L, "Basic", "Available"))
			.build();

		CreateOrdersRequest request = CreateOrdersRequest.builder()
			.memberId(1L)
			.deliveryFeeId(1L)
			.wrappingPaperId(1L)
			.ordersStatusId(1L)
			.ordersDate(LocalDateTime.now())
			.code("valid_code")
			.totalAmount(11000L)
			.deliveryDate(LocalDateTime.now().plusDays(3))
			.bookPurchaseAmount(10000L)
			.deliveryAddress("1234 Test St")
			.senderName("Jane Doe")
			.senderNumber("010-1234-5678")
			.receiverName("John Doe")
			.receiverNumber("010-9876-5432")
			.pointSaveAmount(500L)
			.couponDiscountAmount(0L)
			.useCouponId(null)
			.usePointAmount(0L)
			.build();

		return Orders.builder()
			.member(null)
			.deliveryFee(deliveryFee)
			.wrappingPaper(wrappingPaper)
			.ordersStatus(status)
			.payment(null)
			.request(request)
			.build();
	}

	private OrdersBook createOrdersBook(Orders orders) {
		Book book = spy(Book.class);
		when(book.getTitle()).thenReturn("Sample Book");
		return new OrdersBook(orders, book, 1, 10000L);
	}
}
