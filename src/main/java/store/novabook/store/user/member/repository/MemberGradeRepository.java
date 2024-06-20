package store.novabook.store.user.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.user.member.entity.MemberGrade;

public interface MemberGradeRepository extends JpaRepository<MemberGrade, Long> {
	Optional<MemberGrade> findByName(String name);
}
