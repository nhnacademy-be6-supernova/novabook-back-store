package store.novabook.store.tag.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.common.exception.EntityNotFoundException;

import store.novabook.store.tag.dto.request.CreateTagRequest;
import store.novabook.store.tag.dto.request.UpdateTagRequest;
import store.novabook.store.tag.dto.response.CreateTagResponse;
import store.novabook.store.tag.dto.response.GetTagListResponse;
import store.novabook.store.tag.dto.response.GetTagResponse;
import store.novabook.store.tag.entity.Tag;
import store.novabook.store.tag.repository.TagRepository;
import store.novabook.store.tag.service.TagService;

@Service
@RequiredArgsConstructor
@Transactional
public class TagServiceImpl implements TagService {

	private final TagRepository tagRepository;

	@Override
	@Transactional(readOnly = true)
	public Page<GetTagResponse> getTagAll(Pageable pageable) {
		Page<Tag> tags = tagRepository.findAll(pageable);
		Page<GetTagResponse> tagAllResponses = tags.map(GetTagResponse::fromEntity);

		return new PageImpl<>(tagAllResponses.getContent(), pageable, tags.getTotalElements());
	}

	@Override
	@Transactional(readOnly = true)
	public GetTagListResponse getTagAllList() {
		List<Tag> tags = tagRepository.findAll();
		List<GetTagResponse> tagAllResponses = new ArrayList<>();
		for (Tag tag : tags) {
			tagAllResponses.add(GetTagResponse.fromEntity(tag));
		}
		return GetTagListResponse.builder()
			.getTagResponseList(tagAllResponses)
			.build();
	}

	@Override
	@Transactional(readOnly = true)
	public GetTagResponse getTag(Long id) {
		Tag tag = tagRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(Tag.class,id));
		return GetTagResponse.fromEntity(tag);
	}

	@Override
	public CreateTagResponse createTag(CreateTagRequest createTagRequest) {
		Tag tag = tagRepository.save(new Tag(createTagRequest.name()));
		return new CreateTagResponse(tag.getId());
	}

	@Override
	public void updateTag(Long id, UpdateTagRequest updateTagRequest){
		Tag tag = tagRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(Tag.class,id));
		tag.update(updateTagRequest.name());
	}

	@Override
	public void deleteTag(Long id) {
		tagRepository.deleteById(id);
	}
}
