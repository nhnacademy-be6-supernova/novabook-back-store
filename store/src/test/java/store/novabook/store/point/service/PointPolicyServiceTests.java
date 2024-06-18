package store.novabook.store.point.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import store.novabook.store.point.dto.CreatePointPolicyRequest;
import store.novabook.store.point.dto.GetPointPolicyResponse;
import store.novabook.store.point.entity.PointPolicy;
import store.novabook.store.point.repository.PointPolicyRepository;

public class PointPolicyServiceTests {

	@InjectMocks
	private PointPolicyService pointPolicyService;

	@Mock
	private PointPolicyRepository pointPolicyRepository;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void createPointPolicyTest() {
		PointPolicy pointPolicy = PointPolicy.builder()
			.reviewPointRate(1000)
			.basicPoint(1000)
			.registerPoint(3000)
			.build();

		when(pointPolicyRepository.save(any(PointPolicy.class))).thenReturn(pointPolicy);

		pointPolicyService.savePointPolicy(CreatePointPolicyRequest.builder()
			.reviewPointRate(1000L)
			.basicPoint(1000L)
			.registerPoint(3000L)
			.build());

		verify(pointPolicyRepository, times(1)).save(any(PointPolicy.class));
	}

	@Test
	void getPointPolicyListTest() {
		PointPolicy pointPolicy = PointPolicy.builder()
			.reviewPointRate(1000)
			.basicPoint(1000)
			.registerPoint(3000)
			.build();
		List<PointPolicy> pointPolicyList = Collections.singletonList(pointPolicy);
		Page<PointPolicy> page = new PageImpl<>(pointPolicyList, PageRequest.of(0, 10), pointPolicyList.size());

		when(pointPolicyRepository.findAll(any(Pageable.class))).thenReturn(page);

		Page<GetPointPolicyResponse> result = pointPolicyService.getPointPolicyList(PageRequest.of(0, 10));

		assertNotNull(result);
		assertEquals(1, result.getTotalElements());
		assertEquals(1000, result.getContent().get(0).reviewPointRate());
	}

	@Test
	void getLatestPointPolicyTest() {

		for (int i = 0; i < 10; i++) {
			pointPolicyRepository.save(PointPolicy.builder().id(null)
				.reviewPointRate(6000)
				.basicPoint(6000)
				.registerPoint(6000)
				.build());
		}

		PointPolicy pointPolicy = PointPolicy.builder()
			.reviewPointRate(1000)
			.basicPoint(1000)
			.registerPoint(3000)
			.build();
		List<PointPolicy> pointPolicyList = Collections.singletonList(pointPolicy);

		when(pointPolicyRepository.findTopByOrderByCreatedAtDesc()).thenReturn(pointPolicyList.stream().findFirst());

		GetPointPolicyResponse latestPointPolicy = pointPolicyService.getLatestPointPolicy();

		assertNotNull(latestPointPolicy);

	}
}