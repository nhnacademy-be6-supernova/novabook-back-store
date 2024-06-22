package store.novabook.store.point.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import store.novabook.store.point.dto.CreatePointPolicyRequest;
import store.novabook.store.point.dto.GetPointPolicyResponse;
import store.novabook.store.point.service.PointPolicyService;

@WebMvcTest(PointPolicyController.class)
@ContextConfiguration(classes = {PointPolicyService.class})
@EnableSpringDataWebSupport
public class PointPolicyControllerTest {

	@Autowired
	protected MockMvc mockMvc;

	@MockBean
	private PointPolicyService pointPolicyService;

	@BeforeEach
	void setup() {
		PointPolicyController controller = new PointPolicyController(pointPolicyService);
		mockMvc = MockMvcBuilders.standaloneSetup(controller)
			.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
			.build();
	}

	@Test
	void getPointPoliciesTest() throws Exception {
		GetPointPolicyResponse getPointPolicyResponse = GetPointPolicyResponse.builder()
			.reviewPointRate(1000)
			.basicPoint(1000)
			.registerPoint(3000)
			.build();
		List<GetPointPolicyResponse> getPointPolicyResponseList = Collections.singletonList(getPointPolicyResponse);
		Page<GetPointPolicyResponse> page = new PageImpl<>(getPointPolicyResponseList, PageRequest.of(0, 10),
			getPointPolicyResponseList.size());

		Mockito.when(pointPolicyService.getPointPolicyList(Mockito.any(Pageable.class))).thenReturn(page);

		mockMvc.perform(get("/api/v1/store/point/policies")
				.param("page", "0")
				.param("size", "10")
				.param("sort", "id,desc"))
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	void getLatestPointPolicyTest() throws Exception {
		mockMvc.perform(get("/api/v1/store/point/policies/latest"))
			.andExpect(status().isOk());
	}

	@Test
	void createPointPolicyTest() throws Exception {
		CreatePointPolicyRequest createPointPolicyRequest = CreatePointPolicyRequest.builder()
			.reviewPointRate(1000L)
			.basicPoint(1000L)
			.registerPoint(3000L)
			.build();

		ObjectMapper objectMapper = new ObjectMapper();
		String createPointPolicyRequestJson = objectMapper.writeValueAsString(createPointPolicyRequest);

		mockMvc.perform(post("/point/policies")
				.contentType("application/json")
				.content(createPointPolicyRequestJson))
			.andExpect(status().isCreated());
	}
}
