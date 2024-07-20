package store.novabook.store.tag.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import store.novabook.store.tag.dto.request.CreateTagRequest;
import store.novabook.store.tag.dto.request.UpdateTagRequest;
import store.novabook.store.tag.dto.response.CreateTagResponse;
import store.novabook.store.tag.dto.response.GetTagListResponse;
import store.novabook.store.tag.dto.response.GetTagResponse;
import store.novabook.store.tag.service.TagService;

@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@WebMvcTest(TagController.class)
@WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
class TagControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TagService tagService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void getTagAll() throws Exception {
		// given
		List<GetTagResponse> tagResponses = List.of(
			new GetTagResponse(1L, "태그1"),
			new GetTagResponse(2L, "태그2")
		);
		Page<GetTagResponse> tagPage = new PageImpl<>(tagResponses);

		when(tagService.getTagAll(any())).thenReturn(tagPage);

		mockMvc.perform(get("/api/v1/store/tags")
				.with(csrf())
				.param("page", "0")
				.param("size", "10"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.data[0].id").value(1))
			.andExpect(jsonPath("$.data[0].name").value("태그1"))
			.andExpect(jsonPath("$.data[1].id").value(2))
			.andExpect(jsonPath("$.data[1].name").value("태그2"));

		verify(tagService, times(1)).getTagAll(any());
	}

	@Test
	void getTagAllList() throws Exception {
		// given
		List<GetTagResponse> tagResponses = List.of(
			new GetTagResponse(1L, "Tag1"),
			new GetTagResponse(2L, "Tag2")
		);
		when(tagService.getTagAllList()).thenReturn(new GetTagListResponse(tagResponses));

		// when, then
		mockMvc.perform(get("/api/v1/store/tags"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.body.getTagResponseList[0].id").value(1))
			.andExpect(jsonPath("$.body.getTagResponseList[0].name").value("Tag1"))
			.andExpect(jsonPath("$.body.getTagResponseList[1].id").value(2))
			.andExpect(jsonPath("$.body.getTagResponseList[1].name").value("Tag2"));

		verify(tagService, times(1)).getTagAllList();
	}

	@Test
	void getTag() throws Exception {
		// given
		Long tagId = 1L;
		GetTagResponse tagResponse = new GetTagResponse(tagId, "Tag1");
		when(tagService.getTag(tagId)).thenReturn(tagResponse);

		// when, then
		mockMvc.perform(get("/api/v1/store/tags/{id}", tagId))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.body.id").value(1))
			.andExpect(jsonPath("$.body.name").value("Tag1"));

		verify(tagService, times(1)).getTag(tagId);
	}

	@Test
	void createTag() throws Exception {
		// given
		CreateTagRequest createTagRequest = new CreateTagRequest("New Tag");
		CreateTagResponse createTagResponse = new CreateTagResponse(1L);
		when(tagService.createTag(any(CreateTagRequest.class))).thenReturn(createTagResponse);

		// when, then
		mockMvc.perform(post("/api/v1/store/tags")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(createTagRequest)))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.body.id").value(1));

		verify(tagService, times(1)).createTag(any(CreateTagRequest.class));
	}

	@Test
	void updateTag() throws Exception {
		// given
		Long tagId = 1L;
		UpdateTagRequest updateTagRequest = new UpdateTagRequest("Updated Tag");

		// when, then
		mockMvc.perform(put("/api/v1/store/tags/{id}", tagId)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(updateTagRequest)))
			.andExpect(status().isOk());

		verify(tagService, times(1)).updateTag(eq(tagId), any(UpdateTagRequest.class));
	}

	@Test
	void deleteTag() throws Exception {
		// given
		Long tagId = 1L;

		// when, then
		mockMvc.perform(delete("/api/v1/store/tags/{id}", tagId)
				.with(csrf()))
			.andExpect(status().isOk());

		verify(tagService, times(1)).deleteTag(tagId);
	}
}
