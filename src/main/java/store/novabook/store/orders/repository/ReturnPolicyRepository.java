package store.novabook.store.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.orders.entity.ReturnPolicy;

public interface ReturnPolicyRepository extends JpaRepository<ReturnPolicy, Long> {
	String findContentByOrderByIdDesc();
}
