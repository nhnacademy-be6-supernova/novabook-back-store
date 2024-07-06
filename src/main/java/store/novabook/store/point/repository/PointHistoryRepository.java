package store.novabook.store.point.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import store.novabook.store.point.dto.response.GetPointHistoryResponse;
import store.novabook.store.point.entity.PointHistory;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
	List<PointHistory> findByMemberId(Long memberId);

	Page<GetPointHistoryResponse> findAllByMemberId(Long memberId, Pageable pageable);

	@Query("SELECT SUM(ph.pointAmount) FROM PointHistory ph WHERE ph.member.id = :memberId")
	Long findTotalPointAmountByMemberId(@Param("memberId") Long memberId);
}
