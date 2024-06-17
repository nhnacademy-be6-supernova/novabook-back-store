package store.novabook.store.user.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.user.member.entity.MemberAddress;

public interface MemberAddressRepository extends JpaRepository<MemberAddress, Long> {
}
