package store.novabook.store.search.service;

import java.util.List;

import store.novabook.store.search.document.BookDocument;

public interface BookSearchService {

	// 제목에 특정 단어가 포함된 문서 검색
	List<BookDocument> searchByTitleContaining(String title);
	// 저자에 특정 단어가 포함된 문서 검색
	List<BookDocument> searchByAuthorContaining(String author);
	// 제목에 정확한 문구가 포함된 문서 검색
	List<BookDocument> searchByTitlePhrase(String title);
	// 저자에 정확한 문구가 포함된 문서 검색
	List<BookDocument> searchByAuthorPhrase(String author);

}

