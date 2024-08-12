package store.novabook.store.search.service.impl;

import org.springframework.data.domain.*;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.common.exception.InternalServerException;
import store.novabook.store.search.document.BookDocument;
import store.novabook.store.search.dto.GetBookSearchResponse;
import store.novabook.store.search.repository.BookSearchRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookSearchServiceImpl {
	private final BookSearchRepository bookSearchRepository;
	private final ElasticsearchOperations elasticsearchOperations;

	// 모든 단어 검색
	public Page<GetBookSearchResponse> searchByKeywordContaining(String keyword, Pageable pageable) {
		try{
			Page<BookDocument> bookDocuments = bookSearchRepository.findAll(keyword, pageable);
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


}
