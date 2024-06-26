package store.novabook.store.tag.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.tag.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
	List<Tag> findByIdIn(List<Long> tagIds);
}
