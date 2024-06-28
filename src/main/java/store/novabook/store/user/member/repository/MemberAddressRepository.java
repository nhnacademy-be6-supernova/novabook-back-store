package store.novabook.store.user.member.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.user.member.entity.Member;
import store.novabook.store.user.member.entity.MemberAddress;

public interface MemberAddressRepository extends JpaRepository<MemberAddress, Long> {
	Integer countByMember(Member member);

	Integer countByMemberId(Long memberId);

	List<MemberAddress> findAllByMemberId(Long memberId);
}
