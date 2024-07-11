package store.novabook.store.orders.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.orders.entity.OrdersBook;

public interface OrdersBookRepository extends JpaRepository<OrdersBook, Long>, OrdersBookQueryRepository {
	List<OrdersBook> findAllByOrdersMemberId(Long memberId);

	List<OrdersBook> getOrderDetailByOrdersId(Long ordersId);
}
