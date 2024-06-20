package store.novabook.store.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.order.entity.OrdersBook;

public interface OrdersBookRepository extends JpaRepository<OrdersBook, Long> {
}
