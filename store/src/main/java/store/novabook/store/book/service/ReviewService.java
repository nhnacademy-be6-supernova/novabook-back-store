package store.novabook.store.book.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import store.novabook.store.book.dto.MiniBookResponse;
import store.novabook.store.book.entity.Review;
import store.novabook.store.book.repository.ReviewRepository;

@Service
@RequiredArgsConstructor
public class ReviewService {
	private final ReviewRepository reviewRepository;


	//member id 내가 쓴 책 목록 보기
	public List<MiniBookResponse> myReviews(Long memberId) {
		List<Review> ReviewList = reviewRepository.findByBookMemberId(memberId);
		List<MiniBookResponse> miniBookResponses = new ArrayList<>();
		for (Review review : ReviewList) {
			miniBookResponses.add(MiniBookResponse.from(review.getBook()));
		}
		return miniBookResponses;
	}


}
