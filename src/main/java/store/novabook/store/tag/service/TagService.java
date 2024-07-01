package store.novabook.store.tag.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import store.novabook.store.tag.dto.CreateTagRequest;
import store.novabook.store.tag.dto.CreateTagResponse;
import store.novabook.store.tag.dto.GetTagListResponse;
import store.novabook.store.tag.dto.GetTagResponse;
import store.novabook.store.tag.dto.UpdateTagRequest;

public interface TagService {
	@Transactional(readOnly = true)
	Page<GetTagResponse> getTagAll(Pageable pageable);

	@Transactional(readOnly = true)
	GetTagListResponse getTagAllList();

	@Transactional(readOnly = true)
	GetTagResponse getTag(Long id);

	CreateTagResponse createTag(CreateTagRequest createTagRequest);

	void updateTag(Long id, UpdateTagRequest updateTagRequest);

	void deleteTag(Long id);
}
