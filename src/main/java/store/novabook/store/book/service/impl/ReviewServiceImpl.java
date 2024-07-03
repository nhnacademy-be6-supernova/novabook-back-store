package store.novabook.store.book.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import store.novabook.store.book.dto.request.CreateReviewRequest;
import store.novabook.store.book.dto.request.ReviewImageDTO;
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
import store.novabook.store.common.exception.FailedCreateBookException;
import store.novabook.store.common.image.NHNCloudClient;
import store.novabook.store.common.util.FileConverter;
import store.novabook.store.image.entity.Image;
import store.novabook.store.image.entity.ReviewImage;
import store.novabook.store.image.repository.ImageRepository;
import store.novabook.store.image.repository.ReviewImageRepository;
import store.novabook.store.member.entity.Member;
import store.novabook.store.member.repository.MemberRepository;
import store.novabook.store.orders.entity.OrdersBook;
import store.novabook.store.orders.repository.OrdersBookRepository;
import store.novabook.store.point.entity.PointHistory;
import store.novabook.store.point.entity.PointPolicy;
import store.novabook.store.point.repository.PointHistoryRepository;
import store.novabook.store.point.repository.PointPolicyRepository;

/**
 * 책 리뷰와 관련된 서비스를 제공하는 클래스.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {
	private final ReviewRepository reviewRepository;
	private final OrdersBookRepository ordersBookRepository;
	private final NHNCloudClient nhnCloudClient;
	private final ImageRepository imageRepository;
	private final ReviewImageRepository imageReviewRepository;
	private final PointHistoryRepository pointHistoryRepository;
	private final PointPolicyRepository pointPolicyRepository;
	private final MemberRepository memberRepository;

	@Value("${nhn.cloud.imageManager.accessKey}")
	private String accessKey;

	@Value("${nhn.cloud.imageManager.secretKey}")
	private String secretKey;

	@Value("${nhn.cloud.imageManager.bucketName}")
	private String bucketName;

	private static final String REVIEW_POINT = "리뷰 작성 포인트";

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
	 * @param ordersBookId 주문책 ID
	 * @param request 리뷰 생성 요청 데이터
	 * @return 생성된 리뷰 응답
	 */
	@Override
	public CreateReviewResponse createReview(Long ordersBookId, CreateReviewRequest request, Long memberId) {
		OrdersBook ordersbook = ordersBookRepository.findById(ordersBookId)
			.orElseThrow(() -> new EntityNotFoundException(Book.class, ordersBookId));
		Review review = reviewRepository.save(Review.of(request, ordersbook));
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new EntityNotFoundException(Member.class, memberId));
		PointPolicy pointPolicy = pointPolicyRepository.findTopByOrderByCreatedAtDesc()
			.orElseThrow(() -> new EntityNotFoundException(PointPolicy.class));
		//리뷰 이미지를 저장

		request.reviewImageDTOs().forEach(reviewImageDTO -> {
			String nhnUrl = uploadImage(accessKey, secretKey, bucketName + reviewImageDTO.fileName(), false,
				reviewImageDTO);
			Image image = imageRepository.save(new Image(nhnUrl));
			imageReviewRepository.save(ReviewImage.of(review, image));
		});

		// 리뷰를 달면 포인트 적립
		PointHistory pointHistory = PointHistory.of(pointPolicy, null, member, REVIEW_POINT,
			pointPolicy.getReviewPointRate());
		pointHistoryRepository.save(pointHistory);
		return CreateReviewResponse.from(review);
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

		if (ordersId.equals(review.getOrdersBook().getId())) {
			throw new AlreadyExistException(Review.class);  // 다른 사람이 수정하지 못하도록 예외 처리
		}
		review.update(request);
	}

	public String uploadImage(String appKey, String secretKey, String path, boolean overwrite,
		ReviewImageDTO reviewImageDTO) {

		MultipartFile resource = null;
		try {
			resource = FileConverter.convertToMultipartFile(reviewImageDTO);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		ResponseEntity<String> response = nhnCloudClient.uploadImageMultipartFile(appKey, path, overwrite,
				secretKey, true, resource);
			String jsonResponse = response.getBody();

			// JSON 응답을 파싱하여 URL 필드를 추출
			ObjectMapper objectMapper = new ObjectMapper();

		Map<String, Object> responseMap = null;
		try {
			responseMap = objectMapper.readValue(jsonResponse, Map.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}

		HashMap<String, Object> map = (HashMap<String, Object>)responseMap.get("file");
			return (String)map.get("url");

	}

}