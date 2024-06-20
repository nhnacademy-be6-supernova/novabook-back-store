package store.novabook.store.user.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.user.member.entity.MemberStatus;

public interface MemberStatusRepository extends JpaRepository<MemberStatus, Long> {

}
