package store.novabook.store.tag.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import store.novabook.store.common.exception.NotFoundException;
import store.novabook.store.tag.dto.request.CreateTagRequest;
import store.novabook.store.tag.dto.request.UpdateTagRequest;
import store.novabook.store.tag.dto.response.CreateTagResponse;
import store.novabook.store.tag.dto.response.GetTagListResponse;
import store.novabook.store.tag.dto.response.GetTagResponse;
import store.novabook.store.tag.entity.Tag;
import store.novabook.store.tag.repository.TagRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

	@Mock
	private TagRepository tagRepository;

	@InjectMocks
	private TagServiceImpl tagService;

	private Tag tag1;
	private Tag tag2;

	@BeforeEach
	void setUp() {
		tag1 = Tag.builder().name("Tag1").build();
		tag2 = Tag.builder().name("Tag2").build();
	}

	@Test
	void getTagAll_success() {
		// given
		List<Tag> tags = List.of(tag1, tag2);
		Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
		Pageable pageable = PageRequest.of(0, 10, sort);
		Page<Tag> page = new PageImpl<>(tags);

		when(tagRepository.findAll(pageable)).thenReturn(page);

		// when
		Page<GetTagResponse> result = tagService.getTagAll(pageable);

		// then
		assertEquals(2, result.getContent().size());
		assertEquals("Tag1", result.getContent().get(0).name());
		assertEquals("Tag2", result.getContent().get(1).name());
	}

	@Test
	void getTagAllList_success() {
		// given
		List<Tag> tags = List.of(tag1, tag2);

		when(tagRepository.findAll()).thenReturn(tags);

		// when
		GetTagListResponse result = tagService.getTagAllList();

		// then
		assertEquals(2, result.getTagResponseList().size());
		assertEquals("Tag1", result.getTagResponseList().get(0).name());
		assertEquals("Tag2", result.getTagResponseList().get(1).name());
	}

	@Test
	void getTag_success() {
		// given
		Long tagId = 1L;
		when(tagRepository.findById(tagId)).thenReturn(Optional.of(tag1));

		// when
		GetTagResponse result = tagService.getTag(tagId);

		// then
		assertEquals("Tag1", result.name());
	}

	@Test
	void getTag_tagNotFound_throwNotFoundException() {
		// given
		Long tagId = 999L;
		when(tagRepository.findById(tagId)).thenReturn(Optional.empty());

		// when, then
		assertThrows(NotFoundException.class, () -> tagService.getTag(tagId));
	}

	@Test
	void createTag_success() {
		// given
		CreateTagRequest request = CreateTagRequest.builder().name("New Tag").build();
		Tag newTag = Tag.builder().name(request.name()).build();
		when(tagRepository.save(any(Tag.class))).thenReturn(newTag);

		// when
		CreateTagResponse result = tagService.createTag(request);

		// then
		assertNotNull(result);
	}

	@Test
	void updateTag_success() {
		// given
		Long tagId = 1L;
		UpdateTagRequest request = UpdateTagRequest.builder().name("Updated Tag").build();
		when(tagRepository.findById(tagId)).thenReturn(Optional.of(tag1));

		// when
		tagService.updateTag(tagId, request);

		// then
		assertEquals("Updated Tag", tag1.getName());
	}

	@Test
	void updateTag_tagNotFound_throwNotFoundException() {
		// given
		Long tagId = 999L;
		UpdateTagRequest request = UpdateTagRequest.builder().name("Updated Tag").build();
		when(tagRepository.findById(tagId)).thenReturn(Optional.empty());

		// when, then
		assertThrows(NotFoundException.class, () -> tagService.updateTag(tagId, request));
	}

	@Test
	void deleteTag_success() {
		// given
		Long tagId = 1L;

		// when
		tagService.deleteTag(tagId);

		// then
		verify(tagRepository, times(1)).deleteById(tagId);
	}
}
