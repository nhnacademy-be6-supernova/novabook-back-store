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

/**
 * 책 리뷰와 관련된 서비스를 제공하는 클래스.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {
	private final ReviewRepository reviewRepository;
	private final BookRepository bookRepository;
	private final MemberRepository memberRepository;
	private final OrdersRepository ordersRepository;

	/**
	 * 특정 회원이 작성한 모든 리뷰에 대한 책 목록을 페이지네이션으로 반환한다.
	 * @param memberId 회원 ID
	 * @param pageable 페이징 정보
	 * @return 리뷰된 책 목록의 페이지
	 */
	@Transactional(readOnly = true)
	public Page<SearchBookResponse> myReviews(Long memberId, Pageable pageable) {
		Page<Review> reviewList = reviewRepository.findByOrdersId(memberId, pageable);
		List<SearchBookResponse> searchBookResponses = new ArrayList<>();
		for (Review review : reviewList) {
			searchBookResponses.add(SearchBookResponse.from(review.getBook()));
		}
		return new PageImpl<>(searchBookResponses, pageable, searchBookResponses.size());
	}

	/**
	 * 특정 회원이 작성한 리뷰 목록을 페이지네이션으로 반환한다.
	 * @param memberId 회원 ID
	 * @param pageable 페이징 정보
	 * @return 회원의 리뷰 페이지
	 */
	@Transactional(readOnly = true)
	public Page<GetReviewResponse> membersReviews(Long memberId, Pageable pageable) {
		Page<Review> reviews = reviewRepository.findByOrdersId(memberId, pageable);
		List<GetReviewResponse> reviewResponses = new ArrayList<>();
		for (Review review : reviews) {
			reviewResponses.add(GetReviewResponse.from(review));
		}
		return new PageImpl<>(reviewResponses, pageable, reviewResponses.size());
	}

	/**
	 * 특정 책에 대한 모든 리뷰를 페이지네이션으로 반환한다.
	 * @param bookId 책 ID
	 * @param pageable 페이징 정보
	 * @return 책의 리뷰 페이지
	 */
	@Transactional(readOnly = true)
	public Page<GetReviewResponse> bookReviews(Long bookId, Pageable pageable) {
		Page<Review> reviews = reviewRepository.findByBookId(bookId, pageable);
		List<GetReviewResponse> reviewResponses = new ArrayList<>();
		for (Review review : reviews) {
			reviewResponses.add(GetReviewResponse.from(review));
		}
		return new PageImpl<>(reviewResponses, pageable, reviewResponses.size());
	}

	/**
	 * 새로운 리뷰를 생성하고 그 결과를 반환한다.
	 * @param orderId 주문 ID
	 * @param request 리뷰 생성 요청 데이터
	 * @return 생성된 리뷰 응답
	 */
	public CreateReviewResponse createReview(Long orderId, CreateReviewRequest request) {
		if (existsByBookIdAndMemberId(orderId, request)) {
			throw new AlreadyExistException(Review.class);
		}
		Book book = bookRepository.findById(request.bookId())
			.orElseThrow(() -> new EntityNotFoundException(Book.class, request.bookId()));

		Orders orders = ordersRepository.findById(orderId)
			.orElseThrow(() -> new EntityNotFoundException(Member.class, orderId));

		Review review = reviewRepository.save(Review.of(request, orders, book));

		return CreateReviewResponse.from(review);
	}

	/**
	 * 특정 책 ID와 회원 ID가 일치하는 리뷰가 존재하는지 확인한다.
	 * @param memberId 회원 ID
	 * @param request 리뷰 생성 요청 데이터
	 * @return 존재 여부
	 */
	public boolean existsByBookIdAndMemberId(Long memberId, CreateReviewRequest request) {
		return reviewRepository.existsByOrdersIdAndBookId(request.bookId(), memberId);
	}

	/**
	 * 기존의 리뷰를 업데이트한다.
	 * @param ordersId 주문 ID
	 * @param request 리뷰 업데이트 요청 데이터
	 * @param reviewId 수정할 리뷰의 ID
	 */
	public void updateReview(Long ordersId, UpdateReviewRequest request, Long reviewId) {
		Review review = reviewRepository.findById(reviewId)
			.orElseThrow(() -> new EntityNotFoundException(Review.class, reviewId));

		if(ordersId.equals(review.getOrders().getId())) {
			throw new AlreadyExistException(Review.class);  // 다른 사람이 수정하지 못하도록 예외 처리
		}
		review.update(request);
	}

}
