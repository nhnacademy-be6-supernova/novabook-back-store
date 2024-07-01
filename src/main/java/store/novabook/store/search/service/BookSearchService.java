package store.novabook.store.search.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import store.novabook.store.book.entity.Book;
import store.novabook.store.search.document.BookDocument;
import store.novabook.store.search.repository.BookSearchRepository;

@Service
@RequiredArgsConstructor
public class BookSearchService {

	private final BookSearchRepository bookSearchRepository;

	public List<BookDocument> searchByTitle(String title) {
		return bookSearchRepository.findByTitleContaining(title);
	}

	public List<BookDocument> searchByAuthor(String author) {
		return bookSearchRepository.findByAuthorContaining(author);
	}

	public void save(Book book) {
		bookSearchRepository.save(BookDocument.of(book));
	}

}
