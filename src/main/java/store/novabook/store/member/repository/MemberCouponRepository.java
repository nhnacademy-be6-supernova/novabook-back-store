package store.novabook.store.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.member.entity.MemberCoupon;

public interface MemberCouponRepository extends JpaRepository<MemberCoupon, Long> {
}
