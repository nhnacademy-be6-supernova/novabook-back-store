package store.novabook.store.orders.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import store.novabook.store.orders.dto.request.CreateDeliveryFeeRequest;
import store.novabook.store.orders.dto.response.CreateResponse;
import store.novabook.store.orders.dto.response.GetDeliveryFeeResponse;
import store.novabook.store.orders.service.DeliveryFeeService;

@WithMockUser
@AutoConfigureMockMvc
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(DeliveryFeeController.class)
class DeliveryFeeControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private DeliveryFeeService deliveryFeeService;

	private ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testCreateDeliveryFee() throws Exception {

		CreateDeliveryFeeRequest request = CreateDeliveryFeeRequest.builder()
			.fee(10L)
			.build();

		CreateResponse response = CreateResponse.builder()
			.id(1L)
			.build();

		when(deliveryFeeService.createFee(any(CreateDeliveryFeeRequest.class))).thenReturn(response);

		mockMvc.perform(post("/api/v1/store/orders/delivery-fee")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.body.id").value(1L))
			.andExpect(jsonPath("$.header.isSuccessful").value(true))
			.andExpect(jsonPath("$.header.resultMessage").value("SUCCESS"));
	}

	@Test
	void testGetDeliveryAllPage() throws Exception {
		GetDeliveryFeeResponse deliveryFeeResponse = GetDeliveryFeeResponse.builder()
			.id(1L)
			.fee(10L)
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();

		Page<GetDeliveryFeeResponse> page = new PageImpl<>(Collections.singletonList(deliveryFeeResponse), PageRequest.of(0, 10), 1);
		when(deliveryFeeService.findAllDeliveryFees(any(PageRequest.class))).thenReturn(page);

		mockMvc.perform(get("/api/v1/store/orders/delivery-fee")
				.with(csrf())
				.header("X-USER-ID", "testUser")
				.header("X-USER-ROLE", "admin")
				.param("page", "0")
				.param("size", "10"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.pageNum").value(0))
			.andExpect(jsonPath("$.pageSize").value(10))
			.andExpect(jsonPath("$.totalCount").value(1))
			.andExpect(jsonPath("$.data[0].id").value(1L))
			.andExpect(jsonPath("$.data[0].fee").value(10L))
			.andExpect(jsonPath("$.header.isSuccessful").value(true))
			.andExpect(jsonPath("$.header.resultMessage").value("SUCCESS"));
	}

	@Test
	void testGetDeliveryFeeAllList() throws Exception {
		GetDeliveryFeeResponse deliveryFeeResponse = GetDeliveryFeeResponse.builder()
			.id(1L)
			.fee(10L)
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();

		List<GetDeliveryFeeResponse> list = Collections.singletonList(deliveryFeeResponse);

		when(deliveryFeeService.findAllDeliveryFeeList()).thenReturn(list);

		mockMvc.perform(get("/api/v1/store/orders/delivery-fee")
				.with(csrf())
				.header("X-USER-ID", "testUser")
				.header("X-USER-ROLE", "admin"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.body.getDeliveryFeeResponses[0].id").value(1L))
			.andExpect(jsonPath("$.body.getDeliveryFeeResponses[0].fee").value(10L))
			.andExpect(jsonPath("$.header.isSuccessful").value(true))
			.andExpect(jsonPath("$.header.resultMessage").value("SUCCESS"));
	}

	@Test
	void testGetRecentDeliveryFee() throws Exception {
		GetDeliveryFeeResponse deliveryFeeResponse = GetDeliveryFeeResponse.builder()
			.id(1L)
			.fee(10L)
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();

		when(deliveryFeeService.getRecentDeliveryFee()).thenReturn(deliveryFeeResponse);

		mockMvc.perform(get("/api/v1/store/orders/delivery-fee/recent")
				.with(csrf())
				.header("X-USER-ID", "testUser")
				.header("X-USER-ROLE", "admin"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.body.id").value(1L))
			.andExpect(jsonPath("$.body.fee").value(10L))
			.andExpect(jsonPath("$.body.createdAt").exists())
			.andExpect(jsonPath("$.body.updatedAt").exists())
			.andExpect(jsonPath("$.header.isSuccessful").value(true))
			.andExpect(jsonPath("$.header.resultMessage").value("SUCCESS"));
	}

	@Test
	void testGetDeliveryFee() throws Exception {
		GetDeliveryFeeResponse deliveryFeeResponse = GetDeliveryFeeResponse.builder()
			.id(1L)
			.fee(10L)
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();

		when(deliveryFeeService.getDeliveryFee(1L)).thenReturn(deliveryFeeResponse);

		mockMvc.perform(get("/api/v1/store/orders/delivery-fee/1")
				.with(csrf())
				.header("X-USER-ID", "testUser")
				.header("X-USER-ROLE", "admin"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.body.id").value(1L))
			.andExpect(jsonPath("$.body.fee").value(10L))
			.andExpect(jsonPath("$.body.createdAt").exists())
			.andExpect(jsonPath("$.body.updatedAt").exists())
			.andExpect(jsonPath("$.header.isSuccessful").value(true))
			.andExpect(jsonPath("$.header.resultMessage").value("SUCCESS"));
	}
}
