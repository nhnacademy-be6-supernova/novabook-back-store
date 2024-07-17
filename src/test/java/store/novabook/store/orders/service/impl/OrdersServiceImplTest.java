package store.novabook.store.orders.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.common.exception.NotFoundException;
import store.novabook.store.orders.dto.request.CreateOrdersRequest;
import store.novabook.store.orders.dto.request.CreateOrdersStatusRequest;
import store.novabook.store.orders.dto.request.UpdateOrdersAdminRequest;
import store.novabook.store.orders.dto.response.GetOrdersAdminResponse;
import store.novabook.store.orders.dto.response.GetOrdersResponse;
import store.novabook.store.orders.entity.DeliveryFee;
import store.novabook.store.orders.entity.Orders;
import store.novabook.store.orders.entity.OrdersStatus;
import store.novabook.store.orders.entity.WrappingPaper;
import store.novabook.store.orders.repository.OrdersRepository;
import store.novabook.store.orders.repository.OrdersStatusRepository;
import store.novabook.store.payment.entity.Payment;

import java.util.ArrayList;
import java.util.List;

class OrdersServiceImplTest {

	@Mock
	private OrdersRepository ordersRepository;

	@Mock
	private OrdersStatusRepository ordersStatusRepository;

	@InjectMocks
	private OrdersServiceImpl ordersService;

	private OrdersStatus ordersStatus;
	private Orders orders;
	private Pageable pageable;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		ordersStatus = OrdersStatus.builder()
			.request(new CreateOrdersStatusRequest("PENDING"))
			.build();

		DeliveryFee deliveryFee = mock(DeliveryFee.class);
		WrappingPaper wrappingPaper = mock(WrappingPaper.class);
		Payment payment = mock(Payment.class);

		orders = Orders.builder()
			.member(null)
			.deliveryFee(deliveryFee)
			.wrappingPaper(wrappingPaper)
			.ordersStatus(ordersStatus)
			.payment(payment)
			.request(CreateOrdersRequest.builder()
				.memberId(1L)
				.deliveryFeeId(1L)
				.wrappingPaperId(1L)
				.ordersStatusId(1L)
				.paymentId(1L)
				.ordersDate(LocalDateTime.now())
				.code("ORD123456")
				.totalAmount(50000L)
				.deliveryDate(LocalDateTime.now().plusDays(3))
				.bookPurchaseAmount(45000L)
				.deliveryAddress("1234 Elm Street, Springfield")
				.senderName("John Doe")
				.senderNumber("123-456-7890")
				.receiverName("Jane Doe")
				.receiverNumber("098-765-4321")
				.pointSaveAmount(1000L)
				.couponDiscountAmount(2000L)
				.useCouponId(1L)
				.usePointAmount(500L)
				.build())
			.build();

		pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
	}

	@Test
	void testGetOrdersAdminResponsesAll() {
		List<Orders> ordersList = new ArrayList<>();
		ordersList.add(orders);

		Page<Orders> ordersPage = new PageImpl<>(ordersList);
		when(ordersRepository.findAll(pageable)).thenReturn(ordersPage);

		Page<GetOrdersAdminResponse> response = ordersService.getOrdersAdminResponsesAll(pageable);

		assertNotNull(response);
		assertEquals(1, response.getTotalElements());
		verify(ordersRepository, times(1)).findAll(pageable);
	}

	@Test
	void testUpdate() {
		Long id = 1L;
		Long ordersStatusId = 2L;
		UpdateOrdersAdminRequest request = new UpdateOrdersAdminRequest(ordersStatusId);

		when(ordersStatusRepository.findById(ordersStatusId)).thenReturn(Optional.of(ordersStatus));
		when(ordersRepository.findById(id)).thenReturn(Optional.of(orders));

		ordersService.update(id, request);

		verify(ordersStatusRepository, times(1)).findById(ordersStatusId);
		verify(ordersRepository, times(1)).findById(id);
	}

	@Test
	void testUpdate_OrdersStatusNotFound() {
		Long id = 1L;
		Long ordersStatusId = 2L;
		UpdateOrdersAdminRequest request = new UpdateOrdersAdminRequest(ordersStatusId);

		when(ordersStatusRepository.findById(ordersStatusId)).thenReturn(Optional.empty());

		NotFoundException exception = assertThrows(NotFoundException.class, () -> {
			ordersService.update(id, request);
		});

		assertEquals(ErrorCode.ORDERS_STATUS_NOT_FOUND, exception.getErrorCode());
		verify(ordersStatusRepository, times(1)).findById(ordersStatusId);
		verify(ordersRepository, times(0)).findById(id);
	}

	@Test
	void testUpdate_OrdersNotFound() {
		Long id = 1L;
		Long ordersStatusId = 2L;
		UpdateOrdersAdminRequest request = new UpdateOrdersAdminRequest(ordersStatusId);

		when(ordersStatusRepository.findById(ordersStatusId)).thenReturn(Optional.of(ordersStatus));
		when(ordersRepository.findById(id)).thenReturn(Optional.empty());

		NotFoundException exception = assertThrows(NotFoundException.class, () -> {
			ordersService.update(id, request);
		});

		assertEquals(ErrorCode.ORDERS_NOT_FOUND, exception.getErrorCode());
		verify(ordersStatusRepository, times(1)).findById(ordersStatusId);
		verify(ordersRepository, times(1)).findById(id);
	}

	@Test
	void testGetOrdersById() {
		Long id = 1L;

		when(ordersRepository.findById(id)).thenReturn(Optional.of(orders));

		GetOrdersResponse response = ordersService.getOrdersById(id);

		assertNotNull(response);
		verify(ordersRepository, times(1)).findById(id);
	}

	@Test
	void testGetOrdersById_NotFound() {
		Long id = 1L;

		when(ordersRepository.findById(id)).thenReturn(Optional.empty());

		NotFoundException exception = assertThrows(NotFoundException.class, () -> {
			ordersService.getOrdersById(id);
		});

		assertEquals(ErrorCode.ORDERS_NOT_FOUND, exception.getErrorCode());
		verify(ordersRepository, times(1)).findById(id);
	}
}
