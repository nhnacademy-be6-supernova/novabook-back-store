package store.novabook.store.orders.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import store.novabook.store.orders.dto.request.OrderTemporaryForm;

public interface RedisOrderRepository extends CrudRepository<OrderTemporaryForm, Long> {
	Optional<OrderTemporaryForm> findByOrderCode(String orderCode);
}