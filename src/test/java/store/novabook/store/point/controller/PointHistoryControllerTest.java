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

import store.novabook.store.point.dto.GetPointHistoryResponse;
import store.novabook.store.point.service.PointHistoryService;

@WebMvcTest(PointHistoryController.class)
@ContextConfiguration(classes = {PointHistoryService.class})
@EnableSpringDataWebSupport
public class PointHistoryControllerTest {

	@Autowired
	protected MockMvc mockMvc;

	@MockBean
	private PointHistoryService pointHistoryService;

	@BeforeEach
	void setup() {
		PointHistoryController controller = new PointHistoryController(pointHistoryService);
		mockMvc = MockMvcBuilders.standaloneSetup(controller)
			.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
			.build();
	}

	@Test
	void getPointHistoriesTest() throws Exception {

		GetPointHistoryResponse getPointPolicyResponse = GetPointHistoryResponse.builder()
			.pointContent("pointContent")
			.pointAmount(1000)
			.build();

		List<GetPointHistoryResponse> getPointHistoryResponseList = Collections.singletonList(getPointPolicyResponse);
		Page<GetPointHistoryResponse> page = new PageImpl<>(getPointHistoryResponseList, PageRequest.of(0, 10),
			getPointHistoryResponseList.size());

		Mockito.when(pointHistoryService.getPointHistoryList(Mockito.any(Pageable.class))).thenReturn(page);

		mockMvc.perform(get("/point/histories")
				.param("page", "0")
				.param("size", "10")
				.param("sort", "id,desc"))
			.andDo(print())
			.andExpect(status().isOk());
	}

	// @Test
	// void createPointPolicyTest() throws Exception {
	// 	CreatePointHistoryRequest createPointHistoryRequest = CreatePointHistoryRequest.builder()
	// 		.pointPolicyId(1L)
	// 		.pointContent("pointContent")
	// 		.pointAmount(1000)
	// 		.build();
	//
	// 	mockMvc.perform(post("/point/histories")
	// 			.flashAttr("createPointHistoryRequest", createPointHistoryRequest))
	// 		.andExpect(status().isCreated());
	// }

}
