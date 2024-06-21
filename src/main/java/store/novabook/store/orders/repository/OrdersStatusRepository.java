package store.novabook.store.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.orders.entity.OrdersStatus;

public interface OrdersStatusRepository extends JpaRepository<OrdersStatus, Long> {
}
