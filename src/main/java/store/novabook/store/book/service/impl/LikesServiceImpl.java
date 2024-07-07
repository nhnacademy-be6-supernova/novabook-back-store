package store.novabook.store.book.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.book.dto.response.GetLikeBookResponse;
import store.novabook.store.book.dto.response.LikeBookResponse;
import store.novabook.store.book.entity.Book;
import store.novabook.store.book.entity.Likes;
import store.novabook.store.book.repository.BookRepository;
import store.novabook.store.book.repository.LikesRepository;
import store.novabook.store.book.service.LikesService;
import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.common.exception.NotFoundException;
import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.common.exception.NotFoundException;
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
		return likesList.map(GetLikeBookResponse::from);
	}

	//좋아요가 있는지 없는지 불러오기
	@Override
	public LikeBookResponse getLikeResponse(Long memberId, Long bookId) {
		return new LikeBookResponse(likesRepository.existsByMemberIdAndBookId(memberId, bookId));
	}

	@Override
	public LikeBookResponse likeButton(Long memberId, Long bookId) {
		// 삭제
		if (likesRepository.existsByMemberIdAndBookId(memberId, bookId)) {
			likesRepository.deleteAllByMemberIdAndBookId(memberId, bookId);
			return LikeBookResponse.builder().isLiked(false).build();
		}

		//생성
		Book book = bookRepository.findById(bookId).orElseThrow(() -> new NotFoundException(ErrorCode.BOOK_NOT_FOUND));
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

		likesRepository.save(Likes.of(book, member));

		return LikeBookResponse.builder().isLiked(true).build();

	}

}
