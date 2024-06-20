package store.novabook.store.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.order.entity.DeliveryFee;

public interface DeliveryFeeRepository extends JpaRepository<DeliveryFee, Long> {
	long findTopFeeByOrderByIdDesc();
}
