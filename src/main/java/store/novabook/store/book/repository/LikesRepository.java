package store.novabook.store.book.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.book.entity.Likes;

public interface LikesRepository extends JpaRepository<Likes, Long> {
	int countByBookId(Long bookId);
	Page<Likes> findAllByMemberId(Long memberId, Pageable pageable);
	boolean existsByMemberIdAndBookId(Long memberId, Long bookId);
	Likes findByMemberIdAndBookId(Long memberId, Long bookId);
}
