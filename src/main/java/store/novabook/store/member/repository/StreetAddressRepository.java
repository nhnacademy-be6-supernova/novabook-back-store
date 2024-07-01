package store.novabook.store.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.member.entity.StreetAddress;

public interface StreetAddressRepository extends JpaRepository<StreetAddress, Long> {

	StreetAddress findByZipcodeAndStreetAddress(String zipcode, String streetAddress);
}
