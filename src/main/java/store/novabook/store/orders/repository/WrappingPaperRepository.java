package store.novabook.store.orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.orders.entity.WrappingPaper;

public interface WrappingPaperRepository extends JpaRepository<WrappingPaper, Long> {
}
