package store.novabook.store.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.orders.entity.OrdersBook;

public interface OrdersBookRepository extends JpaRepository<OrdersBook, Long>, OrdersBookQueryRepository {
}
