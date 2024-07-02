package store.novabook.store.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.member.entity.MemberGradeHistory;

public interface MemberGradeHistoryRepository extends JpaRepository<MemberGradeHistory, Long> {
	Optional<MemberGradeHistory> findFirstByMemberIdOrderByCreatedAtDesc(Long memberId);
}
