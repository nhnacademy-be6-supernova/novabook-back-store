package store.novabook.store.point.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.point.entity.PointHistory;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
	List<PointHistory> findByMemberId(Long memberId);
}
