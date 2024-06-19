package store.novabook.store.tag.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.tag.entity.BookTag;

public interface BookTagRepository extends JpaRepository<BookTag, Long> {
	List<BookTag> findAllByBookId(Long bookId);
}
