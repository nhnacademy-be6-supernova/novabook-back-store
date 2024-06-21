package store.novabook.store.orders.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;

import store.novabook.store.exception.EntityNotFoundException;
import store.novabook.store.orders.dto.CreateDeliveryFeeRequest;
import store.novabook.store.orders.dto.CreateResponse;
import store.novabook.store.orders.dto.GetDeliveryFeeResponse;
import store.novabook.store.orders.entity.DeliveryFee;
import store.novabook.store.orders.repository.DeliveryFeeRepository;

class DeliveryFeeServiceTest {

	@Mock
	private DeliveryFeeRepository deliveryFeeRepository;

	@InjectMocks
	private DeliveryFeeService deliveryFeeService;



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
	void testLatestDeliveryFee() {
		long expectedFee = 1000L;
		when(deliveryFeeRepository.findTopFeeByOrderByIdDesc()).thenReturn(expectedFee);

		long actualFee = deliveryFeeService.latestDeliveryFee();

		assertEquals(expectedFee, actualFee);
		verify(deliveryFeeRepository, times(1)).findTopFeeByOrderByIdDesc();
	}

	@Test
	void testFindAllDeliveryFees() {
		DeliveryFee fee1 = new DeliveryFee(CreateDeliveryFeeRequest.builder().fee(1000L).build());
		DeliveryFee fee2 = new DeliveryFee(CreateDeliveryFeeRequest.builder().fee(2000L).build());
		List<DeliveryFee> deliveryFees = Arrays.asList(fee1, fee2);
		when(deliveryFeeRepository.findAll()).thenReturn(deliveryFees);

		Page<GetDeliveryFeeResponse> result = deliveryFeeService.findAllDeliveryFees();

		assertNotNull(result);
		assertEquals(2, result.getTotalElements());
		assertEquals(1000L, result.getContent().get(0).fee());
		assertEquals(2000L, result.getContent().get(1).fee());
		verify(deliveryFeeRepository, times(1)).findAll();
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
