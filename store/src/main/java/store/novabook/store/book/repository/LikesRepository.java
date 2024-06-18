package store.novabook.store.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.book.entity.Likes;

public interface LikesRepository extends JpaRepository<Likes, Long> {
	int countByBookId(Long bookId);
}
