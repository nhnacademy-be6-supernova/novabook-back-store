package store.novabook.store.orders.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.data.domain.Page;

import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.common.exception.NotFoundException;
import store.novabook.store.orders.dto.request.CreateOrdersStatusRequest;
import store.novabook.store.orders.dto.response.CreateResponse;
import store.novabook.store.orders.dto.response.GetOrdersStatusResponse;
import store.novabook.store.orders.entity.OrdersStatus;
import store.novabook.store.orders.repository.OrdersStatusRepository;

class OrdersStatusServiceImplTest {

	@Mock
	private OrdersStatusRepository ordersStatusRepository;

	@InjectMocks
	private OrdersStatusServiceImpl ordersStatusService;

	@Spy
	private OrdersStatus ordersStatusSpy;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testSave() {
		CreateOrdersStatusRequest request = CreateOrdersStatusRequest.builder()
			.name("Pending")
			.build();

		OrdersStatus ordersStatus = spy(new OrdersStatus(request));
		doReturn(1L).when(ordersStatus).getId();

		when(ordersStatusRepository.save(any(OrdersStatus.class))).thenReturn(ordersStatus);

		CreateResponse response = ordersStatusService.save(request);

		assertNotNull(response);
		assertEquals(1L, response.id());
		verify(ordersStatusRepository, times(1)).save(any(OrdersStatus.class));
	}

	@Test
	void testGetOrdersStatusById() {
		Long id = 1L;
		OrdersStatus ordersStatus = spy(new OrdersStatus(CreateOrdersStatusRequest.builder().name("Pending").build()));

		when(ordersStatusRepository.findById(id)).thenReturn(Optional.of(ordersStatus));

		GetOrdersStatusResponse response = ordersStatusService.getOrdersStatus(id);

		assertNotNull(response);
		assertEquals("Pending", response.name());
		verify(ordersStatusRepository, times(1)).findById(id);
	}

	@Test
	void testGetOrdersStatusById_NotFound() {
		Long id = 1L;

		when(ordersStatusRepository.findById(id)).thenReturn(Optional.empty());

		NotFoundException exception = assertThrows(NotFoundException.class, () -> {
			ordersStatusService.getOrdersStatus(id);
		});

		assertEquals(ErrorCode.ORDERS_STATUS_NOT_FOUND, exception.getErrorCode());
		verify(ordersStatusRepository, times(1)).findById(id);
	}

	@Test
	void testSave1() {
		CreateOrdersStatusRequest request = CreateOrdersStatusRequest.builder().name("Processing").build();
		OrdersStatus ordersStatus = spy(new OrdersStatus(request));
		doReturn(1L).when(ordersStatus).getId();

		when(ordersStatusRepository.save(any(OrdersStatus.class))).thenReturn(ordersStatus);

		CreateResponse response = ordersStatusService.save(request);

		assertNotNull(response);
		assertEquals(1L, response.id());
		verify(ordersStatusRepository, times(1)).save(any(OrdersStatus.class));
	}

	@Test
	void testGetOrdersStatus1() {
		OrdersStatus status1 = spy(new OrdersStatus(CreateOrdersStatusRequest.builder().name("Processing").build()));
		OrdersStatus status2 = spy(new OrdersStatus(CreateOrdersStatusRequest.builder().name("Delivered").build()));
		List<OrdersStatus> statusList = Arrays.asList(status1, status2);
		when(ordersStatusRepository.findAll()).thenReturn(statusList);

		Page<GetOrdersStatusResponse> result = ordersStatusService.getOrdersStatus();

		assertNotNull(result);
		assertEquals(2, result.getTotalElements());
		assertEquals("Processing", result.getContent().get(0).name());
		assertEquals("Delivered", result.getContent().get(1).name());
		verify(ordersStatusRepository, times(1)).findAll();
	}

	@Test
	void testGetOrdersStatusById1() {
		Long id = 1L;
		OrdersStatus ordersStatus = spy(
			new OrdersStatus(CreateOrdersStatusRequest.builder().name("Processing").build()));
		when(ordersStatusRepository.findById(id)).thenReturn(Optional.of(ordersStatus));

		GetOrdersStatusResponse response = ordersStatusService.getOrdersStatus(id);

		assertNotNull(response);
		assertEquals("Processing", response.name());
		verify(ordersStatusRepository, times(1)).findById(id);
	}

	@Test
	void testGetOrdersStatusByIdNotFound() {
		Long id = 1L;
		when(ordersStatusRepository.findById(id)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> {
			ordersStatusService.getOrdersStatus(id);
		});

		verify(ordersStatusRepository, times(1)).findById(id);
	}
}
