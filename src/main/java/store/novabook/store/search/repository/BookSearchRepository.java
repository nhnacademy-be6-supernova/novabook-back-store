package store.novabook.store.search.repository;

import java.util.List;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import store.novabook.store.search.document.BookDocument;

public interface BookSearchRepository extends ElasticsearchRepository<BookDocument, Long> {
	// 제목에 특정 단어가 포함된 문서 검색
	List<BookDocument> findAllByTitleContaining(String title);

	// 저자에 특정 단어가 포함된 문서 검색
	List<BookDocument> findAllByAuthorContaining(String author);

	// 제목에 정확한 문구가 포함된 문서 검색
	@Query("{\"match_phrase\": {\"title\": \"?0\"}}")
	List<BookDocument> findByTitlePhrase(String title);

	// 저자에 정확한 문구가 포함된 문서 검색
	@Query("{\"match_phrase\": {\"author\": \"?0\"}}")
	List<BookDocument> findByAuthorPhrase(String author);

}
