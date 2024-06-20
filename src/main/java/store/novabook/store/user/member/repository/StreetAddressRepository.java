package store.novabook.store.user.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.user.member.entity.StreetAddress;

public interface StreetAddressRepository extends JpaRepository<StreetAddress, Long> {
}
