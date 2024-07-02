package store.novabook.store.book.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.book.dto.response.CreateLikesResponse;
import store.novabook.store.book.dto.response.GetLikeBookResponse;
import store.novabook.store.book.entity.Book;
import store.novabook.store.book.entity.Likes;
import store.novabook.store.book.repository.BookRepository;
import store.novabook.store.book.repository.LikesRepository;
import store.novabook.store.book.service.LikesService;
import store.novabook.store.common.exception.EntityNotFoundException;
import store.novabook.store.member.entity.Member;
import store.novabook.store.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class LikesServiceImpl implements LikesService {
	private final LikesRepository likesRepository;
	private final BookRepository bookRepository;
	private final MemberRepository memberRepository;

	//내가 좋아요 누른 책 목록 불러오기
	@Override
	@Transactional(readOnly = true)
	public Page<GetLikeBookResponse> myLikes(Long memberId, Pageable pageable) {
		Page<Likes> likesList = likesRepository.findAllByMemberId(memberId, pageable);
		List<GetLikeBookResponse> responses = new ArrayList<>();
		for (Likes like : likesList) {
			responses.add(GetLikeBookResponse.from(like));
		}
		return new PageImpl<>(responses, pageable, responses.size());
	}

	//생성
	@Override
	public CreateLikesResponse createLikes(Long memberId, Long bookId) {
		Book book = bookRepository.findById(bookId)
			.orElseThrow(() -> new EntityNotFoundException(Book.class,bookId));
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new EntityNotFoundException(Member.class, memberId));
		Likes likes = likesRepository.save(Likes.of(book, member));
		return CreateLikesResponse.from(likes);
	}

	// 삭제
	@Override
	public HttpStatus deleteLikes(Long likesId) {
		likesRepository.deleteById(likesId);
		return HttpStatus.NO_CONTENT;
	}

}
