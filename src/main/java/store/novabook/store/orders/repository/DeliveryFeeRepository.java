package store.novabook.store.orders.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.orders.entity.DeliveryFee;

public interface DeliveryFeeRepository extends JpaRepository<DeliveryFee, Long> {
	long findTopFeeByOrderByIdDesc();
	Optional<DeliveryFee> findByIdOrderByCreatedAtDesc();
}
