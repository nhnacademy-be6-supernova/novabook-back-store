package store.novabook.store.book.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import store.novabook.store.book.dto.response.GetLikeBookResponse;
import store.novabook.store.book.dto.response.LikeBookResponse;
import store.novabook.store.book.entity.Book;
import store.novabook.store.book.entity.Likes;
import store.novabook.store.book.repository.BookRepository;
import store.novabook.store.book.repository.LikesRepository;
import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.common.exception.NotFoundException;
import store.novabook.store.member.entity.Member;
import store.novabook.store.member.repository.MemberRepository;

class LikesServiceImplTest {

	@InjectMocks
	private LikesServiceImpl likesService;

	@Mock
	private LikesRepository likesRepository;

	@Mock
	private BookRepository bookRepository;

	@Mock
	private MemberRepository memberRepository;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testMyLikes() {
		Long memberId = 1L;
		Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
		Pageable pageable = PageRequest.of(0, 10, sort);
		Likes like = Likes.builder()
			.book(Book.builder().title("Title").author("Author").publisher("Publisher").build())
			.member(Member.builder().build())
			.build();
		Page<Likes> likesPage = new PageImpl<>(List.of(like));

		when(likesRepository.findAllByMemberId(memberId, pageable)).thenReturn(likesPage);

		Page<GetLikeBookResponse> result = likesService.myLikes(memberId, pageable);

		assertNotNull(result);
		assertEquals(1, result.getTotalElements());
		assertEquals("Title", result.getContent().get(0).title());
	}

	@Test
	void testGetLikeResponse() {
		Long memberId = 1L;
		Long bookId = 1L;

		when(likesRepository.existsByMemberIdAndBookId(memberId, bookId)).thenReturn(true);

		LikeBookResponse response = likesService.getLikeResponse(memberId, bookId);

		assertNotNull(response);
		assertTrue(response.isLiked());
	}

	@Test
	void testLikeButton() {
		Long memberId = 1L;
		Long bookId = 1L;

		when(likesRepository.existsByMemberIdAndBookId(memberId, bookId)).thenReturn(false);
		when(bookRepository.findById(bookId)).thenReturn(Optional.of(Book.builder().build()));
		when(memberRepository.findById(memberId)).thenReturn(Optional.of(Member.builder().build()));

		LikeBookResponse response = likesService.likeButton(memberId, bookId);

		assertNotNull(response);
		assertTrue(response.isLiked());
		verify(likesRepository, times(1)).save(any(Likes.class));
	}

	@Test
	void testLikeButtonDelete() {
		Long memberId = 1L;
		Long bookId = 1L;

		when(likesRepository.existsByMemberIdAndBookId(memberId, bookId)).thenReturn(true);

		LikeBookResponse response = likesService.likeButton(memberId, bookId);

		assertNotNull(response);
		assertFalse(response.isLiked());
		verify(likesRepository, times(1)).deleteAllByMemberIdAndBookId(memberId, bookId);
	}

	@Test
	void testLikeButtonBookNotFound() {
		Long memberId = 1L;
		Long bookId = 1L;

		when(likesRepository.existsByMemberIdAndBookId(memberId, bookId)).thenReturn(false);
		when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

		NotFoundException exception = assertThrows(NotFoundException.class, () -> {
			likesService.likeButton(memberId, bookId);
		});

		assertEquals(ErrorCode.BOOK_NOT_FOUND, exception.getErrorCode());
	}

	@Test
	void testLikeButtonMemberNotFound() {
		Long memberId = 1L;
		Long bookId = 1L;

		when(likesRepository.existsByMemberIdAndBookId(memberId, bookId)).thenReturn(false);
		when(bookRepository.findById(bookId)).thenReturn(Optional.of(Book.builder().build()));
		when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

		NotFoundException exception = assertThrows(NotFoundException.class, () -> {
			likesService.likeButton(memberId, bookId);
		});

		assertEquals(ErrorCode.MEMBER_NOT_FOUND, exception.getErrorCode());
	}
}
