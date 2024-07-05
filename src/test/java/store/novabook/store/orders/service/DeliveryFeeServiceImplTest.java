package store.novabook.store.orders.service;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import store.novabook.store.common.exception.EntityNotFoundException;

import store.novabook.store.orders.dto.request.CreateDeliveryFeeRequest;
import store.novabook.store.orders.dto.response.CreateResponse;
import store.novabook.store.orders.dto.response.GetDeliveryFeeResponse;
import store.novabook.store.orders.entity.DeliveryFee;
import store.novabook.store.orders.repository.DeliveryFeeRepository;
import store.novabook.store.orders.service.impl.DeliveryFeeServiceImpl;

class DeliveryFeeServiceImplTest {

	@Mock
	private DeliveryFeeRepository deliveryFeeRepository;

	@InjectMocks
	private DeliveryFeeServiceImpl deliveryFeeService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testCreateFee() {
		CreateDeliveryFeeRequest request = CreateDeliveryFeeRequest.builder()
			.fee(1000L)
			.build();
		DeliveryFee deliveryFee = new DeliveryFee(request);
		when(deliveryFeeRepository.save(any(DeliveryFee.class))).thenReturn(deliveryFee);

		CreateResponse response = deliveryFeeService.createFee(request);

		assertNotNull(response);
		assertEquals(deliveryFee.getId(), response.id());
		verify(deliveryFeeRepository, times(1)).save(any(DeliveryFee.class));
	}

	@Test
	void testFindAllDeliveryFees() {
		DeliveryFee fee1 = new DeliveryFee(CreateDeliveryFeeRequest.builder().fee(1000L).build());
		DeliveryFee fee2 = new DeliveryFee(CreateDeliveryFeeRequest.builder().fee(2000L).build());
		List<DeliveryFee> deliveryFees = Arrays.asList(fee1, fee2);
		Page<DeliveryFee> page = new PageImpl<>(deliveryFees, PageRequest.of(0, 10), deliveryFees.size());
		Pageable pageable = PageRequest.of(0, 10);

		when(deliveryFeeRepository.findAll(pageable)).thenReturn(page);

		Page<GetDeliveryFeeResponse> result = deliveryFeeService.findAllDeliveryFees(pageable);

		assertNotNull(result);
		assertEquals(2, result.getTotalElements());
		assertEquals(1000L, result.getContent().get(0).fee());
		assertEquals(2000L, result.getContent().get(1).fee());
		verify(deliveryFeeRepository, times(1)).findAll(any(Pageable.class));
	}

	@Test
	void testGetDeliveryFee() {
		Long id = 1L;
		DeliveryFee deliveryFee = new DeliveryFee(CreateDeliveryFeeRequest.builder().fee(1000L).build());
		when(deliveryFeeRepository.findById(id)).thenReturn(Optional.of(deliveryFee));

		GetDeliveryFeeResponse response = deliveryFeeService.getDeliveryFee(id);

		assertNotNull(response);
		assertEquals(deliveryFee.getFee(), response.fee());
		assertEquals(deliveryFee.getCreatedAt(), response.createdAt());
		assertEquals(deliveryFee.getUpdatedAt(), response.updatedAt());
		verify(deliveryFeeRepository, times(1)).findById(id);
	}

	@Test
	void testNotFoundDeliveryFee() {
		Long id = 1L;
		when(deliveryFeeRepository.findById(id)).thenReturn(Optional.empty());

		assertThrows(EntityNotFoundException.class, () -> deliveryFeeService.getDeliveryFee(id));
		verify(deliveryFeeRepository, times(1)).findById(id);
	}
}
