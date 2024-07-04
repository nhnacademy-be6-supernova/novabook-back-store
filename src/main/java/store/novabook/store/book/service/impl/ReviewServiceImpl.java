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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import store.novabook.store.book.dto.ReviewImageDto;
import store.novabook.store.book.dto.request.CreateReviewRequest;
import store.novabook.store.book.dto.request.ReviewImageDTO;
import store.novabook.store.book.dto.request.UpdateReviewRequest;
import store.novabook.store.book.dto.response.CreateReviewResponse;
import store.novabook.store.book.dto.response.GetOrdersBookReviewIdResponse;
import store.novabook.store.book.dto.response.GetReviewListResponse;
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
import store.novabook.store.common.image.NHNCloudMutilpartClient;
import store.novabook.store.common.response.ImageUploadResponse;
import store.novabook.store.common.util.FileConverter;
import store.novabook.store.common.util.ParamsDTO;
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
	private final NHNCloudMutilpartClient nhnCloudClient;
	private final ImageRepository imageRepository;
	private final ReviewImageRepository reviewImageRepository;
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
	 * 주어진 책 ID와 관련된 모든 리뷰를 읽기 전용으로 조회합니다.
	 *
	 * @param bookId 리뷰를 조회할 책의 ID
	 * @return 해당 책과 관련된 리뷰 목록을 포함하는 {@link GetReviewResponse} 객체 리스트
	 */
	@Override
	@Transactional(readOnly = true)
	public GetReviewListResponse bookReviews(Long bookId) {
		List<ReviewImageDto> reviewImageDtoList = reviewRepository.findReviewByBookId(bookId);

		Map<Long, GetReviewResponse> reviewMap = new HashMap<>();
		for (ReviewImageDto dto : reviewImageDtoList) {
			reviewMap.computeIfAbsent(dto.reviewId(),
				id -> new GetReviewResponse(maskString(dto.nickName()), dto.reviewId(), dto.orderBookId(), dto.content(),
					new ArrayList<>(),
					dto.score(), dto.createdAt())).reviewImages().add(dto.reviewImage());
		}

		return GetReviewListResponse.builder().getReviewResponses(new ArrayList<>(reviewMap.values())).build();
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
		if (reviewRepository.existsByOrdersBookId(ordersBookId)) {
			throw new AlreadyExistException(OrdersBook.class, ordersBookId);
		}

		OrdersBook ordersbook = ordersBookRepository.findById(ordersBookId)
			.orElseThrow(() -> new EntityNotFoundException(OrdersBook.class, ordersBookId));

		Review review = reviewRepository.save(Review.of(request, ordersbook));

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new EntityNotFoundException(Member.class, memberId));

		PointPolicy pointPolicy = pointPolicyRepository.findTopByOrderByCreatedAtDesc()
			.orElseThrow(() -> new EntityNotFoundException(PointPolicy.class));

		//리뷰 이미지를 저장
		String basepath = bucketName + "review";
		//NHN클라우드 설정
		String paramsJson = "{\"basepath\": \"" + basepath + "\", \"overwrite\": true, \"autorename\": \"true\"}";

		List<String> imageList = uploadImage(accessKey, paramsJson, secretKey, request.reviewImageDTOs());

		imageList.forEach(imageUrl -> {
			Image image1 = imageRepository.save(new Image(imageUrl));
			reviewImageRepository.save(new ReviewImage(review, image1));
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

	public List<String> uploadImage(String appKey, String params, String secretKey,
		List<ReviewImageDTO> reviewImageDTO) {
		try {
			List<MultipartFile> resource = FileConverter.convertToMultipartFile(reviewImageDTO);
			String jsonResponse = nhnCloudClient.uploadImagesAndGetRecord(appKey, params, resource, secretKey)
				.getBody();

			// JSON 응답을 파싱하여 URL 필드를 추출
			List<String> response = extractUrls(jsonResponse);

			return response;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static List<String> extractUrls(String jsonString) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(jsonString);

		List<String> urls = new ArrayList<>();

		// 성공 항목(successes)에서 URL 추출
		JsonNode successes = rootNode.path("successes");
		if (successes.isArray()) {
			for (JsonNode item : successes) {
				if (item.has("url")) {
					urls.add(item.get("url").asText());
				}
			}
		}

		return urls;
	}

	public static String maskString(String input) {
		return input != null && input.length() > 3
			? input.substring(0, 3) + "*".repeat(input.length() - 3)
			: input;
	}
}