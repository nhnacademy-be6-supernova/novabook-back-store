package store.novabook.store.orders.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import store.novabook.store.common.exception.NotFoundException;
import store.novabook.store.orders.dto.request.CreateDeliveryFeeRequest;
import store.novabook.store.orders.dto.response.CreateResponse;
import store.novabook.store.orders.dto.response.GetDeliveryFeeResponse;
import store.novabook.store.orders.entity.DeliveryFee;
import store.novabook.store.orders.repository.DeliveryFeeRepository;

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
	@DisplayName("배송료 생성 테스트")
	void testCreateFee() {
		CreateDeliveryFeeRequest request = CreateDeliveryFeeRequest.builder().fee(1000L).build();
		DeliveryFee deliveryFee = new DeliveryFee(request);
		when(deliveryFeeRepository.save(any(DeliveryFee.class))).thenReturn(deliveryFee);

		CreateResponse response = deliveryFeeService.createFee(request);

		assertNotNull(response);
		assertEquals(deliveryFee.getId(), response.id());
		verify(deliveryFeeRepository, times(1)).save(any(DeliveryFee.class));
	}

	@Test
	@DisplayName("모든 배송료 페이지 조회 테스트")
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
	@DisplayName("모든 배송료 목록 조회 테스트")
	void testFindAllDeliveryFeeList() {
		DeliveryFee fee1 = new DeliveryFee(CreateDeliveryFeeRequest.builder().fee(1000L).build());
		DeliveryFee fee2 = new DeliveryFee(CreateDeliveryFeeRequest.builder().fee(2000L).build());
		List<DeliveryFee> deliveryFees = Arrays.asList(fee1, fee2);

		when(deliveryFeeRepository.findAll()).thenReturn(deliveryFees);

		List<GetDeliveryFeeResponse> result = deliveryFeeService.findAllDeliveryFeeList();

		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals(1000L, result.get(0).fee());
		assertEquals(2000L, result.get(1).fee());
		verify(deliveryFeeRepository, times(1)).findAll();
	}

	@Test
	@DisplayName("단일 배송료 조회 테스트")
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
	@DisplayName("존재하지 않는 배송료 조회 시 예외 발생 테스트")
	void testNotFoundDeliveryFee() {
		Long id = 1L;
		when(deliveryFeeRepository.findById(id)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> deliveryFeeService.getDeliveryFee(id));
		verify(deliveryFeeRepository, times(1)).findById(id);
	}

	@Test
	@DisplayName("최근 배송료 조회 테스트")
	void testGetRecentDeliveryFee() {
		DeliveryFee recentFee = new DeliveryFee(CreateDeliveryFeeRequest.builder().fee(3000L).build());
		when(deliveryFeeRepository.findFirstByOrderByCreatedAtDesc()).thenReturn(Optional.of(recentFee));

		GetDeliveryFeeResponse response = deliveryFeeService.getRecentDeliveryFee();

		assertNotNull(response);
		assertEquals(recentFee.getFee(), response.fee());
		assertEquals(recentFee.getCreatedAt(), response.createdAt());
		assertEquals(recentFee.getUpdatedAt(), response.updatedAt());
		verify(deliveryFeeRepository, times(1)).findFirstByOrderByCreatedAtDesc();
	}

	@Test
	@DisplayName("최근 배송료 조회 시 존재하지 않을 경우 예외 발생 테스트")
	void testNotFoundRecentDeliveryFee() {
		when(deliveryFeeRepository.findFirstByOrderByCreatedAtDesc()).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> deliveryFeeService.getRecentDeliveryFee());
		verify(deliveryFeeRepository, times(1)).findFirstByOrderByCreatedAtDesc();
	}
}
