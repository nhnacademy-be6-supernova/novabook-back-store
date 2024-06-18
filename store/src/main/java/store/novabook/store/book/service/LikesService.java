package store.novabook.store.book.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import store.novabook.store.book.dto.MiniBookResponse;
import store.novabook.store.book.entity.Likes;
import store.novabook.store.book.repository.LikesRepository;

@Service
@RequiredArgsConstructor
public class LikesService {
	private final LikesRepository likesRepository;


	public List<MiniBookResponse> myLikes(Long memberId) {
		List<Likes> likesList = likesRepository.findAllByMemberId(memberId);
		List<MiniBookResponse> miniBookResponses = new ArrayList<>();
		for (Likes like : likesList) {
			miniBookResponses.add(MiniBookResponse.from(like.getBook()));
		}
		return miniBookResponses;
	}
}
