package store.novabook.store.category.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import store.novabook.store.category.entity.Category;
import store.novabook.store.tag.entity.Tag;

public interface CategoryRepository extends JpaRepository<Category, Long> {
	List<Category> findAllByOrderByTopCategoryDesc();
	List<Category> findByIdIn(List<Long> categoryIdList);
}
