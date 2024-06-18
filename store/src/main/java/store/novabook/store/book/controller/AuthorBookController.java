package store.novabook.store.book.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import store.novabook.store.book.service.BookService;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class AuthorBookController {
	private final BookService bookService;




}
