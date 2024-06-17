package store.novabook.store.point.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import store.novabook.store.point.entity.PointPolicy;

@Repository
public interface PointPolicyRepository extends JpaRepository<PointPolicy, Long> {

	Optional<PointPolicy> findTopByOrderByCreatedAtDesc();
}
