package store.novabook.store.orders.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.orders.entity.Orders;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
	Page<Orders> findAll(Pageable pageable);
}
