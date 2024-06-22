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

import store.novabook.store.point.dto.GetPointHistoryResponse;
import store.novabook.store.tag.dto.CreateTagRequest;
import store.novabook.store.tag.dto.CreateTagResponse;
import store.novabook.store.tag.dto.GetTagResponse;
import store.novabook.store.tag.dto.UpdateTagRequest;
import store.novabook.store.tag.service.TagService;

@WebMvcTest(TagController.class)
@ContextConfiguration(classes = {TagService.class})
@EnableSpringDataWebSupport
public class TagControllerTest {
	@MockBean
	private TagService tagService;

	@Autowired
	protected MockMvc mockMvc;

	private ObjectMapper objectMapper;

	@BeforeEach
	public void setup() {
		TagController tagController = new TagController(tagService);
		objectMapper = new ObjectMapper();
		PageableHandlerMethodArgumentResolver pageableArgumentResolver = new PageableHandlerMethodArgumentResolver();
		mockMvc = MockMvcBuilders.standaloneSetup(tagController)
			.setCustomArgumentResolvers(pageableArgumentResolver)
			.build();
	}

	@Test
	public void testGetTagAll() throws Exception {
		GetTagResponse response = new GetTagResponse( 1L,"Tag1");
		Pageable pageable = PageRequest.of(0, 10);
		List<GetTagResponse> getTagResponse = Collections.singletonList(response);
		Page<GetTagResponse> page = new PageImpl<>(getTagResponse, pageable, 1);

		when(tagService.getTagAll(pageable)).thenReturn(page);

		mockMvc.perform(get("/tags")
				.contentType(MediaType.APPLICATION_JSON)
				.param("page", "0")
				.param("size", "10"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content[0].id").value(1L))
			.andExpect(jsonPath("$.content[0].name").value("Tag1"));
	}

	@Test
	public void testGetTag() throws Exception {
		GetTagResponse response = new GetTagResponse( 1L,"Tag1");
		when(tagService.getTag(1L)).thenReturn(response);

		mockMvc.perform(get("/tags/1")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(1L))
			.andExpect(jsonPath("$.name").value("Tag1"));
	}

	@Test
	public void testCreateTag() throws Exception {
		CreateTagRequest request = new CreateTagRequest("Tag1");
		CreateTagResponse response = new CreateTagResponse(1L);
		when(tagService.createTag(any(CreateTagRequest.class))).thenReturn(response);

		mockMvc.perform(post("/tags")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(1L));
	}

	@Test
	public void testUpdateTag() throws Exception {
		// UpdateTagRequest request = new UpdateTagRequest(1L, "UpdatedTag");
		// doNothing().when(tagService).updateTag(any(UpdateTagRequest.class));
		//
		// mockMvc.perform(put("/tags")
		// 		.contentType(MediaType.APPLICATION_JSON)
		// 		.content(objectMapper.writeValueAsString(request)))
		// 	.andExpect(status().isOk());
	}

	@Test
	public void testDeleteTag() throws Exception {
		doNothing().when(tagService).deleteTag(1L);

		mockMvc.perform(delete("/tags/1")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}
}
