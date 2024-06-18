package store.novabook.store.point.repository;

import static org.assertj.core.api.Assertions.*;

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
	void createTest() {
		PointPolicy pointPolicy = pointPolicyRepository.save(PointPolicy.builder().id(null)
			.reviewPointRate(1000)
			.basicPoint(1000)
			.registerPoint(3000)
			.build());

		assertThat(pointPolicy).isNotNull();

		entityManager.flush();
		entityManager.clear();
	}

	@Test
	void getTest() {
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
	}

	@Test
	void getAllTest() {

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
	}

	@Test
	void getLatestTest() {
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
	}
}
