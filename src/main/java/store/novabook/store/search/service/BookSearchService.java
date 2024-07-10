package store.novabook.store.search.service;

import java.util.List;

import store.novabook.store.search.dto.GetBookSearchResponse;

public interface BookSearchService {

	// 제목에 특정 단어가 포함된 문서 검색
	List<GetBookSearchResponse> searchByTitleContaining(String title);

	// 저자에 특정 단어가 포함된 문서 검색
	List<GetBookSearchResponse> searchByAuthorContaining(String author);

}

