package store.novabook.store.search.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import store.novabook.store.search.document.BookDocument;

class BookSearchRepositoryTest {

	@Mock
	private BookSearchRepository bookSearchRepository;

	private Page<BookDocument> bookPage;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		BookDocument bookDocument = BookDocument.builder()
			.id(1L)
			.title("Test Book")
			.author("Test Author")
			.publisher("Test Publisher")
			.image("http://test.com/image.jpg")
			.price(1000L)
			.discountPrice(900L)
			.score(4.5)
			.isPackaged(false)
			.review(10)
			.tagList(Collections.singletonList("Test Tag"))
			.categoryList(Collections.singletonList("Test Category"))
			.build();

		bookPage = new PageImpl<>(Collections.singletonList(bookDocument), PageRequest.of(0, 10), 1);
	}

	@Test
	void testFindAll() {
		given(bookSearchRepository.findAll(eq("Test"), any(Pageable.class))).willReturn(bookPage);

		Page<BookDocument> result = bookSearchRepository.findAll("Test", PageRequest.of(0, 10));

		assertEquals(1, result.getTotalElements());
		assertEquals("Test Book", result.getContent().get(0).getTitle());
	}

	@Test
	void testFindAllByAuthorIgnoreCase() {
		given(bookSearchRepository.findAllByAuthorIgnoreCase(eq("Test Author"), any(Pageable.class))).willReturn(bookPage);

		Page<BookDocument> result = bookSearchRepository.findAllByAuthorIgnoreCase("Test Author", PageRequest.of(0, 10));

		assertEquals(1, result.getTotalElements());
		assertEquals("Test Author", result.getContent().get(0).getAuthor());
	}

	@Test
	void testFindAllByPublishIgnoreCase() {
		given(bookSearchRepository.findAllByPublishIgnoreCase(eq("Test Publisher"), any(Pageable.class))).willReturn(bookPage);

		Page<BookDocument> result = bookSearchRepository.findAllByPublishIgnoreCase("Test Publisher", PageRequest.of(0, 10));

		assertEquals(1, result.getTotalElements());
		assertEquals("Test Publisher", result.getContent().get(0).getPublisher());
	}

	@Test
	void testFindAllByCategoryListMatches() {
		given(bookSearchRepository.findAllByCategoryListMatches(eq("Test Category"), any(Pageable.class))).willReturn(bookPage);

		Page<BookDocument> result = bookSearchRepository.findAllByCategoryListMatches("Test Category", PageRequest.of(0, 10));

		assertEquals(1, result.getTotalElements());
		assertEquals("Test Category", result.getContent().get(0).getCategoryList().get(0));
	}
}