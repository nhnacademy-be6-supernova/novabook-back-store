package store.novabook.store.book.repository;

import java.util.List;

import store.novabook.store.book.dto.ReviewImageDto;

public interface ReviewQueryRepository {
	//책의 주문들의 리뷰에 이미지들까지 List로 받음
	List<ReviewImageDto> findReviewByBookId(Long bookId);
}
