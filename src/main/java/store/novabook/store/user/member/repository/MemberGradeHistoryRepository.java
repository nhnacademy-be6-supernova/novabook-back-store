package store.novabook.store.user.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.user.member.entity.MemberGradeHistory;

public interface MemberGradeHistoryRepository extends JpaRepository<MemberGradeHistory, Long> {
}
