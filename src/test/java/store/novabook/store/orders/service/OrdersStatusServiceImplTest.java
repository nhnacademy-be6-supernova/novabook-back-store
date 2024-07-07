package store.novabook.store.orders.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;

import store.novabook.store.common.exception.NotFoundException;
import store.novabook.store.orders.dto.request.CreateOrdersStatusRequest;
import store.novabook.store.orders.dto.request.UpdateOrdersStatusRequest;
import store.novabook.store.orders.dto.response.CreateResponse;
import store.novabook.store.orders.dto.response.GetOrdersStatusResponse;
import store.novabook.store.orders.entity.OrdersStatus;
import store.novabook.store.orders.repository.OrdersStatusRepository;
import store.novabook.store.orders.service.impl.OrdersStatusServiceImpl;

class OrdersStatusServiceImplTest {

	@Mock
	private OrdersStatusRepository ordersStatusRepository;

	@InjectMocks
	private OrdersStatusServiceImpl ordersStatusServiceImpl;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testSave() {
		CreateOrdersStatusRequest request = CreateOrdersStatusRequest.builder().name("Processing").build();
		OrdersStatus ordersStatus = new OrdersStatus(request);
		when(ordersStatusRepository.save(any(OrdersStatus.class))).thenReturn(ordersStatus);

		CreateResponse response = ordersStatusServiceImpl.save(request);

		assertNotNull(response);
		assertEquals(ordersStatus.getId(), response.id());
		verify(ordersStatusRepository, times(1)).save(any(OrdersStatus.class));
	}

	@Test
	void testGetOrdersStatus() {
		OrdersStatus status1 = new OrdersStatus(CreateOrdersStatusRequest.builder().name("Processing").build());
		OrdersStatus status2 = new OrdersStatus(CreateOrdersStatusRequest.builder().name("Delivered").build());
		List<OrdersStatus> statusList = Arrays.asList(status1, status2);
		when(ordersStatusRepository.findAll()).thenReturn(statusList);

		Page<GetOrdersStatusResponse> result = ordersStatusServiceImpl.getOrdersStatus();

		assertNotNull(result);
		assertEquals(2, result.getTotalElements());
		assertEquals("Processing", result.getContent().get(0).name());
		assertEquals("Delivered", result.getContent().get(1).name());
		verify(ordersStatusRepository, times(1)).findAll();
	}

	@Test
	void testGetOrdersStatusById() {
		Long id = 1L;
		OrdersStatus ordersStatus = new OrdersStatus(CreateOrdersStatusRequest.builder().name("Processing").build());
		when(ordersStatusRepository.findById(id)).thenReturn(Optional.of(ordersStatus));

		GetOrdersStatusResponse response = ordersStatusServiceImpl.getOrdersStatus(id);

		assertNotNull(response);
		assertEquals("Processing", response.name());
		verify(ordersStatusRepository, times(1)).findById(id);
	}

	@Test
	void testGetOrdersStatusByIdNotFound() {
		Long id = 1L;
		when(ordersStatusRepository.findById(id)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> {
			ordersStatusServiceImpl.getOrdersStatus(id);
		});

		verify(ordersStatusRepository, times(1)).findById(id);
	}

	@Test
	void testUpdateOrdersStatusNotFound() {
		Long id = 1L;
		UpdateOrdersStatusRequest updateRequest = UpdateOrdersStatusRequest.builder().name("Shipped").build();
		when(ordersStatusRepository.findById(id)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> {
			ordersStatusServiceImpl.updateOrdersStatus(id, updateRequest);
		});

		verify(ordersStatusRepository, times(1)).findById(id);
	}
}
