package store.novabook.store.user.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.user.member.entity.Users;

public interface UsersRepository extends JpaRepository<Users, Long> {
}
