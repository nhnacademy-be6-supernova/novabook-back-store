package store.novabook.store.point.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import store.novabook.store.point.entity.PointPolicy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PointPolicyRepositoryTests {

	@Autowired
	private PointPolicyRepository pointPolicyRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Test
	void createPointPolicyTest() {
		PointPolicy pointPolicy = PointPolicy.builder()
			.reviewPointRate(1000)
			.basicPoint(1000)
			.registerPoint(3000)
			.build();

		PointPolicy savedPointPolicy = pointPolicyRepository.save(pointPolicy);

		assertThat(savedPointPolicy).isNotNull();
		assertThat(savedPointPolicy).isEqualTo(pointPolicy);

		entityManager.flush();
		entityManager.clear();
	}

	@Test
	void getPointPolicyTest() {
		PointPolicy pointPolicy = pointPolicyRepository.save(PointPolicy.builder().id(null)
			.reviewPointRate(1000)
			.basicPoint(1000)
			.registerPoint(3000)
			.build());

		PointPolicy foundPointPolicy = pointPolicyRepository.findById(pointPolicy.getId()).orElse(null);

		assertThat(foundPointPolicy).isNotNull();
		assertThat(foundPointPolicy.getReviewPointRate()).isEqualTo(1000);
		assertThat(foundPointPolicy.getBasicPoint()).isEqualTo(1000);
		assertThat(foundPointPolicy.getRegisterPoint()).isEqualTo(3000);
		entityManager.flush();
		entityManager.clear();
	}

	@Test
	void getAllPolicyTest() {

		for (int i = 0; i < 10; i++) {
			pointPolicyRepository.save(PointPolicy.builder().id(null)
				.reviewPointRate(1000)
				.basicPoint(1000)
				.registerPoint(3000)
				.build());
		}

		Pageable pageable = PageRequest.of(0, 10);
		Page<PointPolicy> pointPolicyList = pointPolicyRepository.findAll(pageable);

		assertThat(pointPolicyList).hasSize(10);
		entityManager.flush();
		entityManager.clear();
	}

	@Test
	void getLatestPointPolicyTest() {
		for (int i = 0; i < 10; i++) {
			pointPolicyRepository.save(PointPolicy.builder().id(null)
				.reviewPointRate(1000)
				.basicPoint(1000)
				.registerPoint(3000)
				.build());
		}

		PointPolicy pointPolicy = pointPolicyRepository.save(PointPolicy.builder().id(null)
			.reviewPointRate(6000)
			.basicPoint(6000)
			.registerPoint(3000)
			.build());

		PointPolicy latestPointPolicy = pointPolicyRepository.findTopByOrderByCreatedAtDesc().orElse(null);

		assertThat(latestPointPolicy).isNotNull();
		assertThat(latestPointPolicy.getId()).isEqualTo(pointPolicy.getId());
		entityManager.flush();
		entityManager.clear();
	}

	@Test
	public void auditingTest() {
		PointPolicy pointPolicy = PointPolicy.builder()
			.reviewPointRate(1000)
			.basicPoint(1000)
			.registerPoint(3000)
			.build();

		PointPolicy savedPointPolicy = pointPolicyRepository.save(pointPolicy);

		assertNotNull(savedPointPolicy.getCreatedAt());
		assertNotNull(savedPointPolicy.getUpdatedAt());
		assertTrue(savedPointPolicy.getCreatedAt().isBefore(LocalDateTime.now()));
		assertTrue(savedPointPolicy.getUpdatedAt().isBefore(LocalDateTime.now()));
	}
}
