package store.novabook.store.orders.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import store.novabook.store.orders.dto.request.OrderTemporaryForm;

public interface RedisOrderRepository extends CrudRepository<OrderTemporaryForm, Long> {
	Optional<OrderTemporaryForm> findByOrderUUID(UUID uuid);

}