package store.novabook.store.book.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.book.dto.PushLikesRequest;
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
	public List<SearchBookResponse> myLikes(Long memberId) {
		List<Likes> likesList = likesRepository.findAllByMemberId(memberId);
		List<SearchBookResponse> searchBookResponses = new ArrayList<>();
		for (Likes like : likesList) {
			searchBookResponses.add(SearchBookResponse.from(like.getBook()));
		}
		return searchBookResponses;
	}

	//없으면 좋아요 생성 아니면 좋아요 제거
	public void pushedLikes(PushLikesRequest request) {
		if(likesRepository.existsByMemberIdAndBookId(request.memberId(),request.bookId())){
			deleteLikes(request);
		}
		createLikes(request);
	}

	//생성
	public void createLikes(PushLikesRequest request) {
		Book book = bookRepository.findById(request.bookId())
			.orElseThrow(()->new EntityNotFoundException(Book.class,request.bookId()));
		Member member = memberRepository.findById(request.memberId())
			.orElseThrow(()->new EntityNotFoundException(Member.class, request.memberId()));
		likesRepository.save(Likes.of(book,member));
	}

	// 삭제
	public void deleteLikes(PushLikesRequest request) {
		Likes likes = likesRepository.findByMemberIdAndBookId(request.memberId(),request.bookId());
		likesRepository.delete(likes);
	}

	//수정은 필요 없을듯

}
