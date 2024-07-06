package store.novabook.store.book.repository;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import store.novabook.store.book.dto.ReviewImageDto;

/**
 * 리뷰에 대한 동적 쿼리를 처리하는 리포지토리 인터페이스입니다.
 *
 * <p>이 인터페이스는 리뷰와 관련된 동적 쿼리를 정의합니다. 주로 책의 ID나 리뷰의 ID를 기준으로 조회하는 메서드들을 포함합니다.
 */
@Transactional(readOnly = true)
public interface ReviewQueryRepository {

	/**
	 * 특정 책의 리뷰와 해당 리뷰에 포함된 이미지 정보를 리스트로 반환합니다.
	 *
	 * @param bookId 조회할 책의 ID
	 * @return List<ReviewImageDto> 책의 리뷰와 이미지 정보를 담고 있는 DTO 리스트
	 */
	List<ReviewImageDto> findReviewByBookId(Long bookId);

	/**
	 * 특정 리뷰의 정보와 해당 리뷰에 포함된 이미지 정보를 리스트로 반환합니다.
	 *
	 * @param reviewId 조회할 리뷰의 ID
	 * @return List<ReviewImageDto> 리뷰의 정보와 이미지 정보를 담고 있는 DTO 리스트
	 */
	List<ReviewImageDto> findReviewByReviewId(Long reviewId);
}
