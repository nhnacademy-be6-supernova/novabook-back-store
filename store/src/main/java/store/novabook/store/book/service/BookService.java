package store.novabook.store.book.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import store.novabook.store.book.repository.BookRepository;

@Service
@RequiredArgsConstructor
public class BookService {
	private final BookRepository bookRepository;
}
