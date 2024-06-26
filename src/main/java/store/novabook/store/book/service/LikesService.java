package store.novabook.store.book.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import store.novabook.store.book.dto.response.CreateLikesResponse;
import store.novabook.store.book.dto.response.GetLikeBookResponse;

public interface LikesService {
	//내가 좋아요 누른 책 목록 불러오기
	@Transactional(readOnly = true)
	Page<GetLikeBookResponse> myLikes(Long memberId, Pageable pageable);

	//생성
	CreateLikesResponse createLikes(Long memberId, Long bookId);

	// 삭제
	HttpStatus deleteLikes(Long likesId);
}
