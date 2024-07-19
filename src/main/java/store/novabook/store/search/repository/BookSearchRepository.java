package store.novabook.store.search.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import store.novabook.store.search.document.BookDocument;

public interface BookSearchRepository extends ElasticsearchRepository<BookDocument, Long> {

	@Query("{\"bool\": {\"should\": [" +
		"{\"match\": {\"title\": \"?0\"}}," +
		"{\"match\": {\"author\": \"?0\"}}," +
		"{\"match\": {\"publisher\": \"?0\"}}" +
		"]}}")
	Page<BookDocument> findAllByKeywordIgnoreCase(String keyword, Pageable pageable);

	@Query("{\"bool\": {\"should\": [" +
		"{\"match\": {\"author\": \"?0\"}}"+
		"]}}")
	Page<BookDocument> findAllByAuthorIgnoreCase(String author, Pageable pageable);

	@Query("{\"bool\": {\"should\": [" +
		"{\"match\": {\"publish\": \"?0\"}}"+
		"]}}")
	Page<BookDocument> findAllByPublishIgnoreCase(String author, Pageable pageable);


	Page<BookDocument> findAllByCategoryListMatches(List<String> categoryList, Pageable pageable);

}
