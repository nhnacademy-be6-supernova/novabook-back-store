package store.novabook.store.point.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import store.novabook.store.order.entity.Orders;
import store.novabook.store.point.entity.PointHistory;
import store.novabook.store.point.entity.PointPolicy;
import store.novabook.store.user.member.entity.Member;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PointHistoryRepositoryTests {

	@Autowired
	private PointHistoryRepository pointHistoryRepository;

	@Autowired
	private PointPolicyRepository pointPolicyRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Test
	void createPointHistoryTest() {
		Orders mockOrders = mock(Orders.class);
		Member mockMember = mock(Member.class);

		PointPolicy pointPolicy = PointPolicy.builder()
			.reviewPointRate(1000)
			.basicPoint(1000)
			.registerPoint(3000)
			.build();
		PointPolicy savedPointPolicy = pointPolicyRepository.save(pointPolicy);

		PointHistory pointHistory = PointHistory.builder()
			.id(null)
			.orders(mockOrders)
			.pointPolicy(savedPointPolicy)
			.member(mockMember)
			.pointContent("pointContent")
			.pointAmount(1000)
			.build();

		PointHistory savedPointHistory = pointHistoryRepository.save(pointHistory);

		assertNotNull(savedPointHistory);
		assertEquals(pointHistory, savedPointHistory);

		entityManager.clear();

	}

	@Test
	public void auditingTest() {
		Orders mockOrders = mock(Orders.class);
		Member mockMember = mock(Member.class);

		PointPolicy pointPolicy = PointPolicy.builder()
			.reviewPointRate(1000)
			.basicPoint(1000)
			.registerPoint(3000)
			.build();
		PointPolicy savedPointPolicy = pointPolicyRepository.save(pointPolicy);

		PointHistory pointHistory = PointHistory.builder()
			.id(null)
			.orders(mockOrders)
			.pointPolicy(savedPointPolicy)
			.member(mockMember)
			.pointContent("pointContent")
			.pointAmount(1000)
			.build();

		PointHistory savedPointHistory = pointHistoryRepository.save(pointHistory);

		assertNotNull(savedPointHistory.getCreatedAt());
		assertNotNull(savedPointHistory.getUpdatedAt());
		assertTrue(savedPointHistory.getCreatedAt().isBefore(LocalDateTime.now()));
		assertTrue(savedPointHistory.getUpdatedAt().isBefore(LocalDateTime.now()));
	}
}
