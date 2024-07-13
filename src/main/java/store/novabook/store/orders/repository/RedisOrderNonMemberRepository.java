package store.novabook.store.orders.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import store.novabook.store.orders.dto.request.OrderTemporaryNonMemberForm;

public interface RedisOrderNonMemberRepository extends CrudRepository<OrderTemporaryNonMemberForm, String> {
}
