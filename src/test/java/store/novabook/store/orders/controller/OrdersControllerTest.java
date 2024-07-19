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
import store.novabook.store.orders.dto.request.UpdateOrdersAdminRequest;
import store.novabook.store.orders.dto.response.GetOrdersAdminResponse;
import store.novabook.store.orders.dto.response.GetOrdersResponse;
import store.novabook.store.orders.service.OrdersService;

@AutoConfigureMockMvc
@WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(OrdersController.class)
class OrdersControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private OrdersService ordersService;

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
	void testGetOrders() throws Exception {
		GetOrdersResponse ordersResponse = GetOrdersResponse.builder().ordersStatusId(1L)
			.memberId(1L)
			.deliveryFeeId(1L)
			.build();

		when(ordersService.getOrdersById(any(Long.class))).thenReturn(ordersResponse);

		mockMvc.perform(get("/api/v1/store/orders/1")
				.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.body.memberId").value(1L))
			.andExpect(jsonPath("$.body.deliveryFeeId").value(1L));
	}

	@Test
	void testUpdateOrders() throws Exception {
		UpdateOrdersAdminRequest request = new UpdateOrdersAdminRequest(1L);

		doNothing().when(ordersService).update(any(Long.class), any(UpdateOrdersAdminRequest.class));

		mockMvc.perform(put("/api/v1/store/orders/1")
				.with(csrf())
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isNoContent());
	}

	@Test
	void testGetOrdersAdmin() throws Exception {
		GetOrdersAdminResponse ordersAdminResponse = GetOrdersAdminResponse.builder()
			.ordersId(1L)
			.totalAmount(1000L)
			.ordersStatusId(1L)
			.build();

		Page<GetOrdersAdminResponse> page = new PageImpl<>(Collections.singletonList(ordersAdminResponse), PageRequest.of(0, 10), 1);
		when(ordersService.getOrdersAdminResponsesAll(any(PageRequest.class))).thenReturn(page);

		mockMvc.perform(get("/api/v1/store/orders/admin")
				.with(csrf())
				.param("page", "0")
				.param("size", "10"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data[0].ordersId").value(1L));
	}
}
