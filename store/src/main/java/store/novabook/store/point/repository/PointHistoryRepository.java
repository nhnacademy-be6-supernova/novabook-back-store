package store.novabook.store.point.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import store.novabook.store.point.entity.PointHistory;

@Repository
public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
}
