package store.novabook.store.orders.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import store.novabook.store.common.security.aop.CheckRole;
import store.novabook.store.common.security.aop.RoleCheckAspect;
import store.novabook.store.orders.dto.request.CreateWrappingPaperRequest;
import store.novabook.store.orders.dto.request.UpdateWrappingPaperRequest;
import store.novabook.store.orders.dto.response.CreateResponse;
import store.novabook.store.orders.dto.response.GetWrappingPaperResponse;
import store.novabook.store.orders.service.WrappingPaperService;

@AutoConfigureMockMvc
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(WrappingPaperController.class)
@WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
class WrappingPaperControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private WrappingPaperService wrappingPaperService;

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
	void testCreateWrappingPaper() throws Exception {
		CreateWrappingPaperRequest request = CreateWrappingPaperRequest.builder().price(1000L).name("테스트").build();
		// 필요한 필드를 request에 설정

		CreateResponse response = new CreateResponse(1L);

		when(wrappingPaperService.createWrappingPaper(any(CreateWrappingPaperRequest.class))).thenReturn(response);

		mockMvc.perform(post("/api/v1/store/orders/wrapping-papers")
				.with(csrf())
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.body.id").value(1L));
	}

	@Test
	void testGetWrappingPaper() throws Exception {
		GetWrappingPaperResponse wrappingPaperResponse = GetWrappingPaperResponse.builder()
			.id(1L)
			.name("Sample Wrapping Paper")
			.price(1000L)
			.build();

		when(wrappingPaperService.getWrappingPaperById(any(Long.class))).thenReturn(wrappingPaperResponse);

		mockMvc.perform(get("/api/v1/store/orders/wrapping-papers/1")
				.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.body.id").value(1L))
			.andExpect(jsonPath("$.body.name").value("Sample Wrapping Paper"))
			.andExpect(jsonPath("$.body.price").value(1000L));
	}

	@Test
	void testUpdateWrappingPaper() throws Exception {
		UpdateWrappingPaperRequest request = UpdateWrappingPaperRequest.builder()
			.price(1000L)
			.name("테스트")
			.status("status")
			.build();
		// 필요한 필드를 request에 설정

		doNothing().when(wrappingPaperService).updateWrappingPaper(any(), any());

		mockMvc.perform(put("/api/v1/store/orders/wrapping-papers/1")
				.with(csrf())
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk());
	}
}
