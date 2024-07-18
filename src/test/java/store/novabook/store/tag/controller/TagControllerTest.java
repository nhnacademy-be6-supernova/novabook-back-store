package store.novabook.store.tag.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import store.novabook.store.tag.dto.request.CreateTagRequest;
import store.novabook.store.tag.dto.response.CreateTagResponse;
import store.novabook.store.tag.dto.response.GetTagResponse;
import store.novabook.store.tag.service.impl.TagServiceImpl;

@WebMvcTest(TagController.class)
@ContextConfiguration(classes = {TagServiceImpl.class})
@EnableSpringDataWebSupport
class TagControllerTest {
	@MockBean
	private TagServiceImpl tagServiceImpl;

	@Autowired
	protected MockMvc mockMvc;

	private ObjectMapper objectMapper;

	@BeforeEach
	void setup() {
		TagController tagController = new TagController(tagServiceImpl);
		objectMapper = new ObjectMapper();
		PageableHandlerMethodArgumentResolver pageableArgumentResolver = new PageableHandlerMethodArgumentResolver();
		mockMvc = MockMvcBuilders.standaloneSetup(tagController)
			.setCustomArgumentResolvers(pageableArgumentResolver)
			.build();
	}

	@Test
	void testGetTag() throws Exception {
		GetTagResponse response = new GetTagResponse(1L, "Tag1");
		when(tagServiceImpl.getTag(1L)).thenReturn(response);

		mockMvc.perform(get("/api/v1/store/tags/1")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(1L))
			.andExpect(jsonPath("$.name").value("Tag1"));
	}

	@Test
	void testCreateTag() throws Exception {
		CreateTagRequest request = new CreateTagRequest("Tag1");
		CreateTagResponse response = new CreateTagResponse(1L);
		when(tagServiceImpl.createTag(any(CreateTagRequest.class))).thenReturn(response);

		mockMvc.perform(post("/api/v1/store/tags")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(1L));
	}

	@Test
	void testDeleteTag() throws Exception {
		doNothing().when(tagServiceImpl).deleteTag(1L);

		mockMvc.perform(delete("/api/v1/store/tags/1")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}
}
