package store.novabook.store.cart.repository;

import org.springframework.data.repository.CrudRepository;

import store.novabook.store.cart.dto.RedisCartHash;

public interface RedisCartRepository extends CrudRepository<RedisCartHash, Object> {
}
