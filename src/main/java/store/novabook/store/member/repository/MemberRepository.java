package store.novabook.store.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	boolean existsByLoginId(String loginId);

	Member findByLoginIdAndLoginPassword(String loginId, String password);

	Member findByLoginId(String loginId);

	Member findByPaycoId(String paycoId);

	boolean existsByEmail(String email);

}
