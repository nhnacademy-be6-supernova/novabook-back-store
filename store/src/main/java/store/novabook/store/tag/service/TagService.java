package store.novabook.store.tag.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.exception.EntityNotFoundException;
import store.novabook.store.tag.dto.CreateTagRequest;
import store.novabook.store.tag.dto.GetTagAllResponse;
import store.novabook.store.tag.dto.UpdateTagRequest;
import store.novabook.store.tag.entity.Tag;
import store.novabook.store.tag.repository.TagRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class TagService {

	private final TagRepository tagRepository;

	public Tag save(Tag tag) {
		return tagRepository.save(tag);
	}

	@Transactional(readOnly = true)
	public Page<GetTagAllResponse> getTagAll(Pageable pageable) {
		Page<Tag> tags = tagRepository.findAll(pageable);
		Page<GetTagAllResponse> tagAllResponses = tags.map(GetTagAllResponse::fromEntity);

		return new PageImpl<>(tagAllResponses.getContent(), pageable, tags.getTotalElements());
	}

	@Transactional(readOnly = true)
	public GetTagAllResponse getTag(Long id) {
		Tag tag = tagRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(Tag.class,id));
		return GetTagAllResponse.fromEntity(tag);
	}

	public void createTag(CreateTagRequest createTagRequest) {
		tagRepository.save(new Tag(createTagRequest.name()));
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
