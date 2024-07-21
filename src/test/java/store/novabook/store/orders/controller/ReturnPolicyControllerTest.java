package store.novabook.store.orders.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;

import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import store.novabook.store.common.security.aop.CheckRole;
import store.novabook.store.common.security.aop.RoleCheckAspect;
import store.novabook.store.orders.dto.request.CreateReturnPolicyRequest;
import store.novabook.store.orders.dto.response.CreateResponse;
import store.novabook.store.orders.dto.response.GetReturnPolicyResponse;
import store.novabook.store.orders.service.ReturnPolicyService;

@AutoConfigureMockMvc
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(ReturnPolicyController.class)
@WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
class ReturnPolicyControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ReturnPolicyService returnPolicyService;

	private ObjectMapper objectMapper = new ObjectMapper();

	@TestConfiguration
	static class TestSecurityConfig {
		@Bean
		@Primary
		public RoleCheckAspect roleCheckAspect() {
			return new RoleCheckAspect() {
				@Override
				public void checkRole(JoinPoint joinPoint, CheckRole checkRole) {
					// Do nothing, just bypass the security check
				}
			};
		}
	}

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testCreateReturnPolicy() throws Exception {
		CreateReturnPolicyRequest request = CreateReturnPolicyRequest.builder()
			.content("하이")
			.build();
		// 필요한 필드를 request에 설정

		CreateResponse response = new CreateResponse(1L);

		when(returnPolicyService.save(any(CreateReturnPolicyRequest.class))).thenReturn(response);

		mockMvc.perform(post("/api/v1/store/orders/return/policy")
				.with(csrf())
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.body.id").value(1L));
	}

	@Test
	void testGetReturnPolicyAll() throws Exception {
		GetReturnPolicyResponse returnPolicyResponse = GetReturnPolicyResponse.builder()
			.id(1L)
			.content("테스트")
			.build();

		Page<GetReturnPolicyResponse> page = new PageImpl<>(Collections.singletonList(returnPolicyResponse),
			PageRequest.of(0, 10), 1);
		when(returnPolicyService.getReturnPolicies()).thenReturn(page);

		mockMvc.perform(get("/api/v1/store/orders/return/policy")
				.with(csrf())
				.param("page", "0")
				.param("size", "10"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data[0].id").value(1L))
			.andExpect(jsonPath("$.data[0].content").value("테스트"))
			.andExpect(jsonPath("$.header.isSuccessful").value(true))
			.andExpect(jsonPath("$.header.resultMessage").value("SUCCESS"));
	}

	@Test
	void testGetReturnPolicy() throws Exception {
		GetReturnPolicyResponse returnPolicyResponse = GetReturnPolicyResponse.builder()
			.id(1L)
			.content("테스트")
			.build();

		when(returnPolicyService.getReturnPolicyById(any(Long.class))).thenReturn(returnPolicyResponse);

		mockMvc.perform(get("/api/v1/store/orders/return/policy/1")
				.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.body.id").value(1L))
			.andExpect(jsonPath("$.body.content").value("테스트"));
	}
}
