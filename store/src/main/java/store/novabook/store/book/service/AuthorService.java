package store.novabook.store.book.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import store.novabook.store.book.dto.AuthorResponse;
import store.novabook.store.book.dto.MiniBookResponse;
import store.novabook.store.book.entity.AuthorBook;
import store.novabook.store.book.repository.AuthorBookRepository;
import store.novabook.store.book.repository.AuthorRepository;
import store.novabook.store.book.repository.BookRepository;

@Service
@RequiredArgsConstructor
public class AuthorService {
	private final AuthorBookRepository authorBookrepository;
	private final AuthorRepository authorRepository;
	private final BookRepository bookRepository;


	//책id로 검색한 AuthorBook에서 작가들만 리턴
	public List<AuthorResponse> getAuthorByBookId(Long bookId) {
		List<AuthorBook> authorBooks = authorBookrepository.findByBookId(bookId);
		List<AuthorResponse> authorResponses = new ArrayList<>();
		authorBooks.forEach( authorBook -> authorResponses.add(AuthorResponse.from(authorBook.getAuthor())));
		return authorResponses;
	}

	//작가id로 검색한 AuthorBook에서 책들 리턴
	public List<MiniBookResponse> getMiniBookByAuthorId(Long authorId) {
		List<AuthorBook> authorBooks = authorBookrepository.findByAuthorId(authorId);
		List<MiniBookResponse> miniBookResponses = new ArrayList<>();
		authorBooks.forEach(authorBook -> miniBookResponses.add(MiniBookResponse.from(authorBook.getBook())));
		return miniBookResponses;
	}


	//작가 등록
	public void save(AuthorBook authorBook) {
		authorBookrepository.save(authorBook);
	}


	//작가 업데이트
	public void updateAuthor(AuthorBook authorBook) {
		authorBookrepository.save(authorBook);
	}

}
