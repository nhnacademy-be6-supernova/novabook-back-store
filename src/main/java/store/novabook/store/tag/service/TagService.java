package store.novabook.store.tag.service;

import java.sql.SQLException;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.exception.EntityNotFoundException;
import store.novabook.store.tag.dto.CreateTagRequest;
import store.novabook.store.tag.dto.CreateTagResponse;
import store.novabook.store.tag.dto.GetTagResponse;
import store.novabook.store.tag.dto.UpdateTagRequest;
import store.novabook.store.tag.entity.Tag;
import store.novabook.store.tag.repository.TagRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class TagService {

	private final TagRepository tagRepository;

	@Transactional(readOnly = true)
	public Page<GetTagResponse> getTagAll(Pageable pageable) {
		Page<Tag> tags = tagRepository.findAll(pageable);
		Page<GetTagResponse> tagAllResponses = tags.map(GetTagResponse::fromEntity);

		return new PageImpl<>(tagAllResponses.getContent(), pageable, tags.getTotalElements());
	}

	@Transactional(readOnly = true)
	public GetTagResponse getTag(Long id) {
		Tag tag = tagRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(Tag.class,id));
		return GetTagResponse.fromEntity(tag);
	}

	public CreateTagResponse createTag(CreateTagRequest createTagRequest) {
		Tag tag = tagRepository.save(new Tag(createTagRequest.name()));
		return new CreateTagResponse(tag.getId());
	}

	public void updateTag(UpdateTagRequest updateTagRequest){
		Tag tag = tagRepository.findById(updateTagRequest.id())
			.orElseThrow(() -> new EntityNotFoundException(Tag.class,updateTagRequest.id()));
		tag.update(updateTagRequest.name());
	}

	public void deleteTag(Long id) {
		tagRepository.deleteById(id);
	}
}
