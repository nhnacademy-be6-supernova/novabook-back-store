package store.novabook.store.tag.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;

import store.novabook.store.tag.dto.request.CreateTagRequest;
import store.novabook.store.tag.dto.response.CreateTagResponse;
import store.novabook.store.tag.dto.response.GetTagResponse;
import store.novabook.store.tag.service.impl.TagServiceImpl;

@WebMvcTest(TagController.class)
@ContextConfiguration(classes = {TagServiceImpl.class})
@EnableSpringDataWebSupport
public class TagControllerTest {
	@MockBean
	private TagServiceImpl tagServiceImpl;

	@Autowired
	protected MockMvc mockMvc;

	private ObjectMapper objectMapper;

	@BeforeEach
	public void setup() {
		TagController tagController = new TagController(tagServiceImpl);
		objectMapper = new ObjectMapper();
		PageableHandlerMethodArgumentResolver pageableArgumentResolver = new PageableHandlerMethodArgumentResolver();
		mockMvc = MockMvcBuilders.standaloneSetup(tagController)
			.setCustomArgumentResolvers(pageableArgumentResolver)
			.build();
	}

	@Test
	public void testGetTag() throws Exception {
		GetTagResponse response = new GetTagResponse( 1L,"Tag1");
		when(tagServiceImpl.getTag(1L)).thenReturn(response);

		mockMvc.perform(get("/api/v1/store/tags/1")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(1L))
			.andExpect(jsonPath("$.name").value("Tag1"));
	}

	@Test
	public void testCreateTag() throws Exception {
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
	public void testDeleteTag() throws Exception {
		doNothing().when(tagServiceImpl).deleteTag(1L);

		mockMvc.perform(delete("/api/v1/store/tags/1")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}
}
