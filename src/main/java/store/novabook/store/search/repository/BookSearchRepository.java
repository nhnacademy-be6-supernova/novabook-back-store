package store.novabook.store.search.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import store.novabook.store.search.document.BookDocument;

public interface BookSearchRepository extends ElasticsearchRepository<BookDocument, Long> {
	List<BookDocument> findByTitleContaining(String title);
	List<BookDocument> findByAuthorContaining(String author);
}
