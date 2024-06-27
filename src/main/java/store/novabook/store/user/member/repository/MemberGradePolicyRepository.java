package store.novabook.store.user.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.user.member.entity.MemberGradePolicy;

public interface MemberGradePolicyRepository extends JpaRepository<MemberGradePolicy, Long> {
	Optional<MemberGradePolicy> findByName(String name);
}