package store.novabook.store.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.order.entity.Orders;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
}
