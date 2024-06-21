package store.novabook.store.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.order.entity.ReturnPolicy;

public interface ReturnPolicyRepository extends JpaRepository<ReturnPolicy, Long> {
	String findContentByOrderByIdDesc();
}
