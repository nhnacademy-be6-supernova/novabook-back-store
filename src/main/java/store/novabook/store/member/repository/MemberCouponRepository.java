package store.novabook.store.member.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.member.entity.MemberCoupon;

public interface MemberCouponRepository extends JpaRepository<MemberCoupon, Long> {
	List<MemberCoupon> findByMemberId(Long memberId);
}
