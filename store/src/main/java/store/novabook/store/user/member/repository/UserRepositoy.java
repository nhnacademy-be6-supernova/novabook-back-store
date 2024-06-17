package store.novabook.store.user.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.user.member.entity.User;

public interface UserRepositoy extends JpaRepository<User, Long> {

}
