package store.novabook.store.member.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import store.novabook.store.member.service.GuestService;
import store.novabook.store.orders.dto.request.GetGuestOrderHistoryRequest;
import store.novabook.store.orders.dto.response.GetOrderDetailResponse;

@WithMockUser(username = "guest", roles = {"GUEST"})
@AutoConfigureMockMvc
@WebMvcTest(controllers = GuestController.class)
@MockBean(JpaMetamodelMappingContext.class)
class GuestControllerTest {

	@MockBean
	private GuestService guestService;

	@Autowired
	private MockMvc mockMvc;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void getOrder() throws Exception {
		GetGuestOrderHistoryRequest request = new GetGuestOrderHistoryRequest("guestEmail@example.com", "orderNumber");

		GetOrderDetailResponse response = GetOrderDetailResponse.builder()
			.ordersId(1L)
			.ordersStatusId(1L)
			.ordersStatusName("Completed")
			.bookTitle(List.of("Book Title 1", "Book Title 2"))
			.quantity(2)
			.deliveryFee(500L)
			.wrappingFee(300L)
			.receiverName("John Doe")
			.receiverNumber("1234567890")
			.receiverAddress("123 Main St, Apt 101")
			.expectedDeliveryDate(LocalDateTime.now())
			.totalPrice(2000L)
			.couponDiscountAmount(200L)
			.finalAmount(2100L)
			.pointSaveAmount(100L)
			.build();

		when(guestService.getOrderGuest(any(GetGuestOrderHistoryRequest.class))).thenReturn(response);

		mockMvc.perform(post("/api/v1/store/guest")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.body.ordersId").value(1L))
			.andExpect(jsonPath("$.body.ordersStatusId").value(1L))
			.andExpect(jsonPath("$.body.ordersStatusName").value("Completed"))
			.andExpect(jsonPath("$.body.bookTitle[0]").value("Book Title 1"))
			.andExpect(jsonPath("$.body.bookTitle[1]").value("Book Title 2"))
			.andExpect(jsonPath("$.body.quantity").value(2))
			.andExpect(jsonPath("$.body.deliveryFee").value(500L))
			.andExpect(jsonPath("$.body.wrappingFee").value(300L))
			.andExpect(jsonPath("$.body.receiverName").value("John Doe"))
			.andExpect(jsonPath("$.body.receiverNumber").value("1234567890"))
			.andExpect(jsonPath("$.body.receiverAddress").value("123 Main St, Apt 101"))
			.andExpect(jsonPath("$.body.expectedDeliveryDate").exists())
			.andExpect(jsonPath("$.body.totalPrice").value(2000L))
			.andExpect(jsonPath("$.body.couponDiscountAmount").value(200L))
			.andExpect(jsonPath("$.body.finalAmount").value(2100L))
			.andExpect(jsonPath("$.body.pointSaveAmount").value(100L));
	}
}
