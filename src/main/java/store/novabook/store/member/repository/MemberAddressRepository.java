package store.novabook.store.member.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.member.entity.Member;
import store.novabook.store.member.entity.MemberAddress;

public interface MemberAddressRepository extends JpaRepository<MemberAddress, Long> {
	Integer countByMember(Member member);

	Integer countByMemberId(Long memberId);

	List<MemberAddress> findAllByMemberId(Long memberId);
}
