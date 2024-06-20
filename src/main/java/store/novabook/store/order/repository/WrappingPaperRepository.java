package store.novabook.store.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.order.entity.WrappingPaper;

public interface WrappingPaperRepository extends JpaRepository<WrappingPaper, Long> {
}
