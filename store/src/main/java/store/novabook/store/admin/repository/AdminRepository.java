package store.novabook.store.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.admin.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
