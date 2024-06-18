package store.novabook.store.point.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
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
		// Given
		GetPointPolicyResponse getPointPolicyResponse = GetPointPolicyResponse.builder()
			.reviewPointRate(1000)
			.basicPoint(1000)
			.registerPoint(3000)
			.build();
		List<GetPointPolicyResponse> getPointPolicyResponseList = Collections.singletonList(getPointPolicyResponse);
		Page<GetPointPolicyResponse> page = new PageImpl<>(getPointPolicyResponseList, PageRequest.of(0, 10),
			getPointPolicyResponseList.size());

		// When
		Mockito.when(pointPolicyService.getPointPolicyList(Mockito.any(Pageable.class))).thenReturn(page);

		mockMvc.perform(get("/point/policies")
				.param("page", "0")
				.param("size", "10")
				.param("sort", "id,desc"))
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	void getLatestPointPolicyTest() throws Exception {
		mockMvc.perform(get("/point/policies/latest"))
			.andExpect(status().isOk());
	}

	// @Test
	// void testGetMileStoneAdd() throws Exception {
	// 	mockMvc.perform(get("/milestone/add"))
	// 		.andExpect(status().isOk());
	// }
	//
	// @Test
	// void testPostMileStoneAdd() throws Exception {
	//
	// 	MilestoneRequest milestoneRequest = new MilestoneRequest("m1", LocalDate.now().plusDays(1), LocalDate.now().plusDays(3));
	//
	// 	Project project = new Project(1L, "project name 1", ProjectStatus.ACTIVE);
	//
	// 	Mockito.when(projectRepository.findById(1L)).thenReturn(java.util.Optional.of(project));
	//
	// 	MockHttpSession session = new MockHttpSession();
	// 	session.setAttribute("user", 1L);
	//
	//
	// 	mockMvc.perform(post("/milestone/add")
	// 			.flashAttr("milestoneRequest", milestoneRequest)
	// 			.session(session))
	// 		.andExpect(status().isOk());
	// }
}
