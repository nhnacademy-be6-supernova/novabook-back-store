package store.novabook.store.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.order.entity.OrdersStatus;

public interface OrdersStatusRepository extends JpaRepository<OrdersStatus, Long> {
}
