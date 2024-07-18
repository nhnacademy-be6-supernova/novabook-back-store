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
import store.novabook.store.orders.dto.request.CreateOrdersStatusRequest;
import store.novabook.store.orders.dto.response.CreateResponse;
import store.novabook.store.orders.dto.response.GetOrdersStatusResponse;
import store.novabook.store.orders.service.OrdersStatusService;

@AutoConfigureMockMvc
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(OrdersStatusController.class)
@WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
class OrdersStatusControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private OrdersStatusService ordersStatusService;

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
	void testCreateOrdersStatus() throws Exception {
		CreateOrdersStatusRequest request = CreateOrdersStatusRequest.builder().name("테스트").build();
		// 필요한 필드를 request에 설정

		CreateResponse response = new CreateResponse(1L);

		when(ordersStatusService.save(any(CreateOrdersStatusRequest.class))).thenReturn(response);

		mockMvc.perform(post("/api/v1/store/orders/status")
				.with(csrf())
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.body.id").value(1L));
	}

	@Test
	void testGetOrdersStatus() throws Exception {
		GetOrdersStatusResponse response = GetOrdersStatusResponse.builder()
			.id(1L)
			.name("테스트")
			.build();

		when(ordersStatusService.getOrdersStatus(any(Long.class))).thenReturn(response);

		mockMvc.perform(get("/api/v1/store/orders/status/1")
				.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.body.id").value(1L))
			.andExpect(jsonPath("$.body.name").value("테스트"));
	}
}
