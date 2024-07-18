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
import store.novabook.store.orders.dto.PaymentType;
import store.novabook.store.orders.dto.request.PaymentRequest;
import store.novabook.store.orders.service.impl.OrdersSagaManagerImpl;

@AutoConfigureMockMvc
@WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(OrdersSagaController.class)
class OrdersSagaControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private OrdersSagaManagerImpl ordersSagaManager;

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
	void testCreateOrder() throws Exception {
		PaymentRequest paymentRequest = PaymentRequest.builder()
			.orderCode("123")
			.memberId(1L)
			.type(PaymentType.TOSS)
			.build();
		// 필요한 필드를 paymentRequest에 설정

		doNothing().when(ordersSagaManager).orderInvoke(any(PaymentRequest.class));

		mockMvc.perform(post("/api/v1/store/orders/saga")
				.with(csrf())
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(paymentRequest)))
			.andExpect(status().isOk());
	}
}
