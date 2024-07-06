package store.novabook.store.book.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.book.dto.request.CreateReviewRequest;
import store.novabook.store.book.dto.request.UpdateReviewRequest;
import store.novabook.store.book.dto.response.CreateReviewResponse;
import store.novabook.store.book.dto.response.GetOrdersBookReviewIdResponse;
import store.novabook.store.book.dto.response.GetReviewResponse;
import store.novabook.store.book.dto.response.SearchBookResponse;
import store.novabook.store.book.entity.Book;
import store.novabook.store.book.entity.Review;
import store.novabook.store.book.repository.BookRepository;
import store.novabook.store.book.repository.ReviewRepository;
import store.novabook.store.book.service.ReviewService;
import store.novabook.store.common.exception.AlreadyExistException;
import store.novabook.store.common.exception.EntityNotFoundException;
import store.novabook.store.exception.BadRequestException;
import store.novabook.store.exception.ErrorCode;
import store.novabook.store.member.repository.MemberRepository;
import store.novabook.store.orders.entity.OrdersBook;
import store.novabook.store.orders.repository.OrdersBookRepository;

/**
 * 책 리뷰와 관련된 서비스를 제공하는 클래스.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {
	private final ReviewRepository reviewRepository;
	private final BookRepository bookRepository;
	private final MemberRepository memberRepository;
	private final OrdersBookRepository ordersBookRepository;

	/**
	 * 특정 회원이 작성한 모든 리뷰에 대한 책 목록을 페이지네이션으로 반환한다.
	 * @param memberId 회원 ID
	 * @param pageable 페이징 정보
	 * @return 리뷰된 책 목록의 페이지
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<SearchBookResponse> myReviews(Long memberId, Pageable pageable) {
		Page<Review> reviewList = reviewRepository.findByOrdersBookId(memberId, pageable);
		List<SearchBookResponse> searchBookResponses = new ArrayList<>();
		//TODO review 수정중
		for (Review review : reviewList) {
			// searchBookResponses.add(SearchBookResponse.from(review.getBook()));
		}
		return new PageImpl<>(searchBookResponses, pageable, searchBookResponses.size());
	}

	/**
	 * 특정 회원이 작성한 리뷰 목록을 페이지네이션으로 반환한다.
	 * @param memberId 회원 ID
	 * @param pageable 페이징 정보
	 * @return 회원의 리뷰 페이지
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<GetReviewResponse> membersReviews(Long memberId, Pageable pageable) {
		Page<Review> reviews = reviewRepository.findByOrdersBookId(memberId, pageable);
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
	@Override
	@Transactional(readOnly = true)
	public Page<GetReviewResponse> bookReviews(Long bookId, Pageable pageable) {
		// Page<Review> reviews = reviewRepository.findByBookId(bookId, pageable);
		// List<GetReviewResponse> reviewResponses = new ArrayList<>();
		// for (Review review : reviews) {
		// 	reviewResponses.add(GetReviewResponse.from(review));
		// }
		// return new PageImpl<>(reviewResponses, pageable, reviewResponses.size());
		return null;
	}

	/**
	 * 특정 회원이 구매한 책에 목록을 페이지네이션으로 반환한다.
	 * @param memberId 회원 아이디
	 * @param pageable 페이징 정보
	 * @return 구매한 책 목록 페이지
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<GetOrdersBookReviewIdResponse> getOrdersBookReviewIds(Long memberId, Pageable pageable) {
		Page<Review> reviews = reviewRepository.findAllByOrdersBookOrdersMemberId(memberId, pageable);
		List<GetOrdersBookReviewIdResponse> reviewResponses = new ArrayList<>();
		for (Review review : reviews) {
			reviewResponses.add(GetOrdersBookReviewIdResponse.form(review));
		}
		return new PageImpl<>(reviewResponses, pageable, reviewResponses.size());
	}

	/**
	 * 새로운 리뷰를 생성하고 그 결과를 반환한다.
	 * @param orderId 주문 ID
	 * @param request 리뷰 생성 요청 데이터
	 * @return 생성된 리뷰 응답
	 */
	@Override
	public CreateReviewResponse createReview(Long orderId, CreateReviewRequest request) {
		if (existsByBookIdAndMemberId(orderId, request)) {
			throw new BadRequestException(ErrorCode.ALREADY_EXISTS_REVIEW);
			// throw new AlreadyExistException(Review.class);
		}
		OrdersBook ordersbook = ordersBookRepository.findById(request.bookId())
			.orElseThrow(() -> new EntityNotFoundException(Book.class, request.bookId()));

		// TODO: OrdersBook 변경 필요
		Review review = reviewRepository.save(Review.of(request, ordersbook));

		return CreateReviewResponse.from(review);
	}

	/**
	 * 특정 책 ID와 회원 ID가 일치하는 리뷰가 존재하는지 확인한다.
	 * @param memberId 회원 ID
	 * @param request 리뷰 생성 요청 데이터
	 * @return 존재 여부
	 */
	@Override
	public boolean existsByBookIdAndMemberId(Long memberId, CreateReviewRequest request) {
		// return reviewRepository.existsByOrdersBookIdAndBookId(request.bookId(), memberId);
		return false;
	}

	/**
	 * 기존의 리뷰를 업데이트한다.
	 * @param ordersId 주문 ID
	 * @param request 리뷰 업데이트 요청 데이터
	 * @param reviewId 수정할 리뷰의 ID
	 */
	@Override
	public void updateReview(Long ordersId, UpdateReviewRequest request, Long reviewId) {
		Review review = reviewRepository.findById(reviewId)
			.orElseThrow(() -> new EntityNotFoundException(Review.class, reviewId));

		// TODO 이거 무슨 코드인가요??
		if (ordersId.equals(review.getOrdersBook().getId())) {
			// throw new BadRequestException(ErrorCode.)
			throw new AlreadyExistException(Review.class);  // 다른 사람이 수정하지 못하도록 예외 처리
		}
		review.update(request);
	}

}