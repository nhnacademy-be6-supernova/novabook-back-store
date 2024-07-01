package store.novabook.store.tag.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import store.novabook.store.tag.dto.CreateTagRequest;
import store.novabook.store.tag.dto.CreateTagResponse;
import store.novabook.store.tag.dto.GetTagResponse;
import store.novabook.store.tag.dto.UpdateTagRequest;
import store.novabook.store.tag.entity.Tag;
import store.novabook.store.tag.repository.TagRepository;
import store.novabook.store.tag.service.impl.TagServiceImpl;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {
	@Mock
	private TagRepository tagRepository;

	@InjectMocks
	private TagServiceImpl tagServiceImpl;

	private Tag tag;
	private CreateTagRequest createTagRequest;
	private UpdateTagRequest updateTagRequest;


	@BeforeEach
	void setUp() {
		createTagRequest = new CreateTagRequest("tag");
		updateTagRequest = new UpdateTagRequest("tagUpdate");
		tag = new Tag(createTagRequest.name());
	}

	@Test
	void getTagAll() {
		List<Tag> tags = Collections.singletonList(tag);
		Page<Tag> page = new PageImpl<>(tags, PageRequest.of(0, 10), tags.size());

		when(tagRepository.findAll(any(Pageable.class))).thenReturn(page);

		Page<GetTagResponse> result = tagServiceImpl.getTagAll(PageRequest.of(0, 10));

		assertNotNull(result);
		assertEquals(1, result.getTotalElements());

	}

	@Test
	void getTag() {
		when(tagRepository.findById(1L)).thenReturn(Optional.ofNullable(tag));
		GetTagResponse getTagResponse = tagServiceImpl.getTag(1L);
		assertEquals(tag.getId(), getTagResponse.id());
		verify(tagRepository, times(1)).findById(anyLong());
	}

	@Test
	@DisplayName("createTag - 성공")
	void createTag() {
		when(tagRepository.save(any(Tag.class))).thenReturn(tag);
		CreateTagResponse createTagResponse = tagServiceImpl.createTag(createTagRequest);
		assertNotNull(createTagResponse);
		assertEquals(tag.getId(), createTagResponse.id());
		verify(tagRepository, times(1)).save(any(Tag.class));
	}

	// @Test
	// void updateTag() {
	// 	// when(tagRepository.findById(1L)).thenReturn(Optional.ofNullable(tag));
	// 	// tagService.updateTag(updateTagRequest);
	// 	// assertEquals(tag.getName(), updateTagRequest.name());
	//
	// }

	@Test
	void deleteTag() {
		tagServiceImpl.deleteTag(1L);
		verify(tagRepository, times(1)).deleteById(anyLong());
	}
}