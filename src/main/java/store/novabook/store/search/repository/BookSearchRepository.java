package store.novabook.store.search.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import store.novabook.store.search.document.BookDocument;

public interface BookSearchRepository extends ElasticsearchRepository<BookDocument, Long> {

	@Query("{\"bool\": {\"should\": [" +
		"{\"match\": {\"title\": {\"query\": \"?0\", \"boost\": 2.0}}}," +
		"{\"match\": {\"author\": {\"query\": \"?0\", \"boost\": 1.5}}}," +
		"{\"match\": {\"publisher\": {\"query\": \"?0\", \"boost\": 1.0}}}" +
		"]}}")
	Page<BookDocument> findAll(String keyword, Pageable pageable);

	@Query("{\"bool\": {\"should\": [" +
		"{\"match\": {\"author\": \"?0\"}}"+
		"]}}")
	Page<BookDocument> findAllByAuthorIgnoreCase(String author, Pageable pageable);

	@Query("{\"bool\": {\"should\": [" +
		"{\"match\": {\"publish\": \"?0\"}}"+
		"]}}")
	Page<BookDocument> findAllByPublishIgnoreCase(String author, Pageable pageable);


	Page<BookDocument> findAllByCategoryListMatches(String categoryList, Pageable pageable);

}
