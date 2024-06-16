package store.novabook.store.point.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.point.entity.PointPolicy;

public interface PointPolicyRepository extends JpaRepository<PointPolicy, Long> {
}
