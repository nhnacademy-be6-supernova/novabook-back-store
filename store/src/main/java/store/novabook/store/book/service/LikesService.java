package store.novabook.store.book.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.book.dto.CreateLikesRequest;
import store.novabook.store.book.dto.CreateLikesResponse;
import store.novabook.store.book.dto.SearchBookResponse;
import store.novabook.store.book.entity.Book;
import store.novabook.store.book.entity.Likes;
import store.novabook.store.book.repository.BookRepository;
import store.novabook.store.book.repository.LikesRepository;
import store.novabook.store.exception.EntityNotFoundException;
import store.novabook.store.user.member.entity.Member;
import store.novabook.store.user.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class LikesService {
	private final LikesRepository likesRepository;
	private final BookRepository bookRepository;
	private final MemberRepository memberRepository;

	//내가 좋아요 누른 책 목록 불러오기
	@Transactional(readOnly = true)
	public Page<SearchBookResponse> myLikes(Long memberId, Pageable pageable) {
		Page<Likes> likesList = likesRepository.findAllByMemberId(memberId, pageable);
		List<SearchBookResponse> searchBookResponses = new ArrayList<>();
		for (Likes like : likesList) {
			searchBookResponses.add(SearchBookResponse.from(like.getBook()));
		}
		return new PageImpl<>(searchBookResponses, pageable, searchBookResponses.size());
	}

	//없으면 좋아요 생성 아니면 좋아요 제거

	//생성
	public CreateLikesResponse createLikes(CreateLikesRequest request) {
		Book book = bookRepository.findById(request.bookId())
			.orElseThrow(() -> new EntityNotFoundException(Book.class, request.bookId()));
		Member member = memberRepository.findById(request.memberId())
			.orElseThrow(() -> new EntityNotFoundException(Member.class, request.memberId()));
		Likes likes = likesRepository.save(Likes.of(book, member));
		return CreateLikesResponse.from(likes);
	}

	// 삭제
	public HttpStatus deleteLikes(Long likesId) {

		likesRepository.deleteById(likesId);
		return HttpStatus.NO_CONTENT;
	}

	//수정은 필요 없을듯

}
