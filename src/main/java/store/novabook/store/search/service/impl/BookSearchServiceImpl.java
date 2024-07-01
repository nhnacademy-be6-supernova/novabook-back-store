package store.novabook.store.search.service.impl;

import java.util.List;

import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import store.novabook.store.book.entity.Book;
import store.novabook.store.search.document.BookDocument;
import store.novabook.store.search.repository.BookSearchRepository;
import store.novabook.store.search.service.BookSearchService;

@Service
@RequiredArgsConstructor
public class BookSearchServiceImpl implements BookSearchService {
	private final BookSearchRepository bookSearchRepository;

	// 제목에 특정 단어가 포함된 문서 검색
	public List<BookDocument> searchByTitleContaining(String title) {
		return bookSearchRepository.findAllByTitleContaining(title);
	}

	// 저자에 특정 단어가 포함된 문서 검색
	public List<BookDocument> searchByAuthorContaining(String author) {
		return bookSearchRepository.findAllByAuthorContaining(author);
	}

	// 제목에 정확한 문구가 포함된 문서 검색
	public List<BookDocument> searchByTitlePhrase(String title) {
		return bookSearchRepository.findByTitlePhrase(title);
	}

	// 저자에 정확한 문구가 포함된 문서 검색
	public List<BookDocument> searchByAuthorPhrase(String author) {
		return bookSearchRepository.findByAuthorPhrase(author);
	}

	public void save(Book book) {
		bookSearchRepository.save(BookDocument.of(book));
	}

}
