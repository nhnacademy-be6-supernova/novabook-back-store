package store.novabook.store.book.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import store.novabook.store.book.dto.response.GetLikeBookResponse;
import store.novabook.store.book.dto.response.LikeBookResponse;

public interface LikesService {
	//내가 좋아요 누른 책 목록 불러오기
	Page<GetLikeBookResponse> myLikes(Long memberId, Pageable pageable);

	LikeBookResponse getLikeResponse(Long memberId, Long bookId);

	LikeBookResponse likeButton(Long memberId, Long bookId);
}
