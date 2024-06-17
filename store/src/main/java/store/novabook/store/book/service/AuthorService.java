package store.novabook.store.book.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import store.novabook.store.book.entity.AuthorBook;
import store.novabook.store.book.repository.AuthorBookRepository;
import store.novabook.store.book.repository.AuthorRepository;

@Service
@RequiredArgsConstructor
public class AuthorService {
	private final AuthorBookRepository repository;
	private final AuthorRepository authorRepository;

	//TODO 저자가 작성한 책 리스트

	// 작가 저장
	public AuthorBook save(AuthorBook authorBook) {
		return repository.save(authorBook);
	}

	//작가 정보
	public AuthorBook findById(Long id) {
		return repository.findById(id).orElse(null);
	}

}
