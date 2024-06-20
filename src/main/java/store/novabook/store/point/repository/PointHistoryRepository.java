package store.novabook.store.point.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.point.entity.PointHistory;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
}
