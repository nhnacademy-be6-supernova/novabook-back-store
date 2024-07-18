package store.novabook.store.point.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import store.novabook.store.common.exception.NotFoundException;
import store.novabook.store.member.entity.Member;
import store.novabook.store.member.repository.MemberRepository;
import store.novabook.store.orders.entity.Orders;
import store.novabook.store.orders.repository.OrdersRepository;
import store.novabook.store.point.dto.request.CreatePointHistoryRequest;
import store.novabook.store.point.dto.response.GetPointHistoryResponse;
import store.novabook.store.point.entity.PointHistory;
import store.novabook.store.point.entity.PointPolicy;
import store.novabook.store.point.repository.PointHistoryRepository;
import store.novabook.store.point.repository.PointPolicyRepository;
import store.novabook.store.point.service.impl.PointHistoryServiceImpl;

class PointHistoryServiceImplTests {

	@InjectMocks
	private PointHistoryServiceImpl pointHistoryServiceImpl;

	@Mock
	private PointPolicyRepository pointPolicyRepository;

	@Mock
	private PointHistoryRepository pointHistoryRepository;

	@Mock
	private MemberRepository memberRepository;

	@Mock
	private OrdersRepository ordersRepository;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@AfterEach
	void tearDown() {
		try {
			MockitoAnnotations.openMocks(this).close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	void createPointHistoryTest() {
		PointPolicy mockPointPolicy = mock(PointPolicy.class);
		Orders mockOrders = mock(Orders.class);
		Member mockMember = mock(Member.class);

		when(pointPolicyRepository.findById(anyLong())).thenReturn(java.util.Optional.of(mockPointPolicy));
		when(ordersRepository.findById(anyLong())).thenReturn(java.util.Optional.of(mockOrders));
		when(memberRepository.findById(anyLong())).thenReturn(java.util.Optional.of(mockMember));

		pointHistoryServiceImpl.createPointHistory(CreatePointHistoryRequest.builder()
			.ordersId(mockOrders.getId())
			.pointPolicyId(mockPointPolicy.getId())
			.memberId(mockMember.getId())
			.pointContent("pointContent")
			.pointAmount(1000L)
			.build());

		verify(pointHistoryRepository, times(1)).save(any(PointHistory.class));
	}

	@Test
	void getPointHistoryList() {

		PointHistory pointHistory = PointHistory.builder()
			.pointPolicy(mock(PointPolicy.class))
			.member(mock(Member.class))
			.pointContent("pointContent")
			.pointAmount(1000)
			.build();
		List<PointHistory> pointHistoryList = Collections.singletonList(pointHistory);
		Page<PointHistory> page = new PageImpl<>(pointHistoryList, PageRequest.of(0, 10), pointHistoryList.size());

		when(pointHistoryRepository.findAll(any(Pageable.class))).thenReturn(page);

		Page<GetPointHistoryResponse> result = pointHistoryServiceImpl.getPointHistoryList(PageRequest.of(0, 10));

		assertNotNull(result);
		assertEquals(1, result.getTotalElements());
		assertEquals("pointContent", result.getContent().getFirst().pointContent());
	}

	@Test
	void getPointHistoryListTest_EntityNotFoundException() {
		when(pointHistoryRepository.findAll(any(Pageable.class))).thenReturn(Page.empty());
		assertThrows(NotFoundException.class, this::callGetPointHistoryList);
	}

	private void callGetPointHistoryList() {
		PageRequest pageRequest = PageRequest.of(0, 10);
		pointHistoryServiceImpl.getPointHistoryList(pageRequest);
	}
}
