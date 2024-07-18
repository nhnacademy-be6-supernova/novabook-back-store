package store.novabook.store.search.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import store.novabook.store.book.repository.BookQueryRepository;
import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.common.exception.InternalServerException;
import store.novabook.store.search.document.BookDocument;
import store.novabook.store.search.dto.GetBookSearchResponse;
import store.novabook.store.search.repository.BookSearchRepository;

@Service
@RequiredArgsConstructor
public class BookSearchServiceImpl {
	private final BookSearchRepository bookSearchRepository;
	private final ElasticsearchOperations elasticsearchOperations;
	private final BookQueryRepository bookQueryRepository;

	// 모든 단어 검색
	public Page<GetBookSearchResponse> searchByKeywordContaining(String keyword, Pageable pageable) {
		try{
			Page<BookDocument> bookDocuments = bookSearchRepository.findAllByKeywordIgnoreCase(keyword, pageable);
			return bookDocuments.map(GetBookSearchResponse::of);
		} catch (Exception e){
			throw new InternalServerException(ErrorCode.INVALID_REQUEST_ARGUMENT);
		}
	}

	// 저자에 특정 단어가 포함된 문서 검색
	public Page<GetBookSearchResponse> searchByAuthorContaining(String author, Pageable pageable) {
		try{
			Page<BookDocument> bookDocuments = bookSearchRepository.findAllByAuthorIgnoreCase(author, pageable);
			return bookDocuments.map(GetBookSearchResponse::of);
		} catch (Exception e){
			throw new InternalServerException(ErrorCode.INVALID_REQUEST_ARGUMENT);
		}

	}

	// 출판사에 특정 단어가 포함된 문서 검색
	public Page<GetBookSearchResponse> searchByPublishContaining(String author, Pageable pageable) {
		try{
			Page<BookDocument> bookDocuments = bookSearchRepository.findAllByPublishIgnoreCase(author, pageable);
			return bookDocuments.map(GetBookSearchResponse::of);
		} catch (Exception e){
			throw new InternalServerException(ErrorCode.INVALID_REQUEST_ARGUMENT);
		}
	}

	// 카테고리 특정 단어가 포함된 문서 검색
	public Page<GetBookSearchResponse> searchByCategoryListContaining(String category, Pageable pageable) {
		try{
			Page<BookDocument> bookDocuments = bookSearchRepository.findAllByCategoryListMatches(category, pageable);
			return bookDocuments.map(GetBookSearchResponse::of);
		} catch (Exception e){
			throw new InternalServerException(ErrorCode.INVALID_REQUEST_ARGUMENT);
		}
	}


	public List<BookDocument> searchByTagsContaining(Pageable pageable){
		List<BookDocument> bookDocuments = bookQueryRepository.getBookDocuments(pageable);
		bookSearchRepository.saveAll(bookDocuments);
		return bookDocuments;
	}


}
