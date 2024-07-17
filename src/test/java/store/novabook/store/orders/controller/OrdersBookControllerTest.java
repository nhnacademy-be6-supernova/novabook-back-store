package store.novabook.store.orders.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
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

import store.novabook.store.book.dto.response.GetOrdersBookReviewIdResponse;
import store.novabook.store.common.security.aop.CheckRole;
import store.novabook.store.common.security.aop.RoleCheckAspect;
import store.novabook.store.orders.dto.request.CreateOrdersBookRequest;
import store.novabook.store.orders.dto.request.CreateOrdersRequest;
import store.novabook.store.orders.dto.response.GetOrderDetailResponse;
import store.novabook.store.orders.dto.response.GetOrdersBookResponse;
import store.novabook.store.orders.entity.Orders;
import store.novabook.store.orders.entity.OrdersBook;
import store.novabook.store.orders.service.OrdersBookService;

@AutoConfigureMockMvc
@WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(OrdersBookController.class)
class OrdersBookControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private OrdersBookService ordersBookService;

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
	void testGetOrdersBookAll() throws Exception {
		GetOrdersBookResponse ordersBookResponse = GetOrdersBookResponse.builder()
			.ordersId(1L)
			.firstBookTitle("Book Title")
			.extraBookCount(2L)
			.totalAmount(1000L)
			.orderStatus("PENDING")
			.createdAt(LocalDateTime.now())
			.build();

		Page<GetOrdersBookResponse> page = new PageImpl<>(Collections.singletonList(ordersBookResponse), PageRequest.of(0, 10), 1);
		when(ordersBookService.getOrdersBookByMemberId(any(), any())).thenReturn(page);

		mockMvc.perform(get("/api/v1/store/orders/book/member/orders")
				.with(csrf())
				.param("page", "0")
				.param("size", "10"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data[0].ordersId").value(1L));
	}

	@Test
	void testGetOrderDetails() throws Exception {
		Orders orders = Orders.builder()
			.request(CreateOrdersRequest.builder()
				.totalAmount(1000L)
				.bookPurchaseAmount(1000L)
				.build())
			.build();

		OrdersBook ordersBook = spy(OrdersBook.builder()
			.request(CreateOrdersBookRequest.builder()
				.quantity(10)
				.price(1000L)
				.build())
			.orders(orders)
			.build());

		doReturn(orders).when(ordersBook).getOrders();

		GetOrderDetailResponse orderDetailResponse = GetOrderDetailResponse.builder()
			.ordersId(1L)
			.build();

		when(ordersBookService.getOrderDetail(any(Long.class))).thenReturn(orderDetailResponse);

		mockMvc.perform(get("/api/v1/store/orders/book/detail/1").with(csrf()))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.body.ordersId").value(1L));
	}

	@Test
	void testGetOrdersBookReviewIdByMemberId() throws Exception {
		GetOrdersBookReviewIdResponse reviewIdResponse = GetOrdersBookReviewIdResponse.builder()
			.ordersId(1L)
			.bookId(1L)
			.reviewId(1L)
			.build();

		Page<GetOrdersBookReviewIdResponse> page = new PageImpl<>(Collections.singletonList(reviewIdResponse), PageRequest.of(0, 10), 1);
		when(ordersBookService.getOrdersBookReviewByMemberId(any(), any())).thenReturn(page);

		mockMvc.perform(get("/api/v1/store/orders/book/members").with(csrf())
				.param("page", "0")
				.param("size", "10"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data[0].ordersId").value(1L));
	}
}
