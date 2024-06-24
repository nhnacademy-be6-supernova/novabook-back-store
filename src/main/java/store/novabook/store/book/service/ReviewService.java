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
import store.novabook.store.book.dto.CreateReviewResponse;
import store.novabook.store.book.dto.GetReviewResponse;
import store.novabook.store.book.dto.SearchBookResponse;
import store.novabook.store.book.dto.UpdateReviewRequest;
import store.novabook.store.book.entity.Book;
import store.novabook.store.book.entity.Review;
import store.novabook.store.book.repository.BookRepository;
import store.novabook.store.book.repository.ReviewRepository;
import store.novabook.store.common.exception.AlreadyExistException;
import store.novabook.store.common.exception.EntityNotFoundException;
import store.novabook.store.orders.entity.Orders;
import store.novabook.store.orders.repository.OrdersRepository;
import store.novabook.store.user.member.entity.Member;
import store.novabook.store.user.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {
	private final ReviewRepository reviewRepository;
	private final BookRepository bookRepository;
	private final MemberRepository memberRepository;
	private final OrdersRepository ordersRepository;

	//member id 내가 쓴 책 목록 보기
	@Transactional(readOnly = true)
	public Page<SearchBookResponse> myReviews(Long memberId, Pageable pageable) {
		Page<Review> reviewList = reviewRepository.findByMemberId(memberId, pageable);
		List<SearchBookResponse> searchBookResponses = new ArrayList<>();
		for (Review review : reviewList) {
			searchBookResponses.add(SearchBookResponse.from(review.getBook()));
		}
		return new PageImpl<>(searchBookResponses, pageable, searchBookResponses.size());
	}

	//내가쓴 리뷰 보기
	@Transactional(readOnly = true)
	public Page<GetReviewResponse> membersReviews(Long memberId, Pageable pageable) {
		Page<Review> reviews = reviewRepository.findByMemberId(memberId, pageable);
		List<GetReviewResponse> reviewResponses = new ArrayList<>();
		for (Review review : reviews) {
			reviewResponses.add(GetReviewResponse.from(review));
		}
		return new PageImpl<>(reviewResponses, pageable, reviewResponses.size());
	}

	//책의 리뷰 보기
	@Transactional(readOnly = true)
	public Page<GetReviewResponse> bookReviews(Long bookId, Pageable pageable) {
		Page<Review> reviews = reviewRepository.findByBookId(bookId, pageable);
		List<GetReviewResponse> reviewResponses = new ArrayList<>();
		for (Review review : reviews) {
			reviewResponses.add(GetReviewResponse.from(review));
		}
		return new PageImpl<>(reviewResponses, pageable, reviewResponses.size());
	}

	// 생성
	public CreateReviewResponse createReview(Long orderId, CreateReviewRequest request) {
		if (existsByBookIdAndMemberId(orderId, request)) {
			throw new AlreadyExistException(Review.class);
		}
		Book book = bookRepository.findById(request.bookId())
			.orElseThrow(() -> new EntityNotFoundException(Book.class, request.bookId()));

		Orders orders = ordersRepository.findById(orderId)
			.orElseThrow(() -> new EntityNotFoundException(Member.class, orderId));

		Review review = reviewRepository.save(Review.toEntity(request, orders, book));

		return CreateReviewResponse.from(review);
	}

	public boolean existsByBookIdAndMemberId(Long memberId, CreateReviewRequest request) {
		return reviewRepository.existsByMemberIdAndBookId(request.bookId(), memberId);
	}

	// 수정
	public void updateReview(Long ordersId, UpdateReviewRequest request, Long reviewId) {
		Review review = reviewRepository.findById(reviewId)
			.orElseThrow(() -> new EntityNotFoundException(Review.class, reviewId));

		if(ordersId.equals(review.getOrders().getId())) {
			throw new AlreadyExistException(Review.class);// 나말고 다른사람이 수정못하게 하는 코드
		}
		review.updateEntity(request);
	}

}
