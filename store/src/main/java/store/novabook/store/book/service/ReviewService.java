package store.novabook.store.book.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.book.dto.CreateReviewRequest;
import store.novabook.store.book.dto.SearchBookResponse;
import store.novabook.store.book.dto.UpdateReviewRequest;
import store.novabook.store.book.entity.Book;
import store.novabook.store.book.entity.Review;
import store.novabook.store.book.repository.BookRepository;
import store.novabook.store.book.repository.ReviewRepository;
import store.novabook.store.exception.AlreadyExistException;
import store.novabook.store.exception.EntityNotFoundException;
import store.novabook.store.user.member.entity.Member;
import store.novabook.store.user.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {
	private final ReviewRepository reviewRepository;
	private final BookRepository bookRepository;
	private final MemberRepository memberRepository;

	//member id 내가 쓴 책 목록 보기
	@Transactional(readOnly = true)
	public Page<SearchBookResponse> myReviews(Long memberId, Pageable pageable) {
		Page<Review> ReviewList = reviewRepository.findByMemberId(memberId,pageable);
		List<SearchBookResponse> searchBookResponses = new ArrayList<>();
		for (Review review : ReviewList) {
			searchBookResponses.add(SearchBookResponse.from(review.getBook()));
		}
		return new PageImpl<>(searchBookResponses, pageable, searchBookResponses.size());
	}

	// 생성
	public void createReview(CreateReviewRequest request) {
		if(existsByBookIdAndMemberId(request)){
			throw new AlreadyExistException(Review.class);
		}
		Book book = bookRepository.findById(request.bookId())
			.orElseThrow(()->new EntityNotFoundException(Book.class,request.bookId()));
		Member member = memberRepository.findById(request.memberId())
			.orElseThrow(()->new EntityNotFoundException(Member.class, request.memberId()));

		reviewRepository.save(Review.toEntity(request,member,book));
	}

	public boolean existsByBookIdAndMemberId(CreateReviewRequest request) {
 		return reviewRepository.existsByMemberIdAndBookId(request.bookId(), request.memberId());
	}
	public boolean existsByBookIdAndMemberId(UpdateReviewRequest request) {
		return reviewRepository.existsByMemberIdAndBookId(request.bookId(), request.memberId());
	}


	// 수정
	public void updateReview(UpdateReviewRequest request) {
		if(!existsByBookIdAndMemberId(request)){
			throw new EntityNotFoundException(Review.class);
		}
		Review review= reviewRepository.findByMemberIdAndBookId(request.memberId(), request.bookId());

		review.updateEntity(request);
	}


}
