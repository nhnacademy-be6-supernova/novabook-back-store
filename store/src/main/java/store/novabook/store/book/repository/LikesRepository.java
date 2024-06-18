package store.novabook.store.book.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.book.entity.Likes;

public interface LikesRepository extends JpaRepository<Likes, Long> {
	int countByBookId(Long bookId);
	List<Likes> findAllByMemberId(Long memberId);
}
