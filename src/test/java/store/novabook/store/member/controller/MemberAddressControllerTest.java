package store.novabook.store.member.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

import store.novabook.store.common.security.aop.CurrentMembersArgumentResolver;
import store.novabook.store.member.dto.request.CreateMemberAddressRequest;
import store.novabook.store.member.dto.request.UpdateMemberAddressRequest;
import store.novabook.store.member.dto.response.CreateMemberAddressResponse;
import store.novabook.store.member.dto.response.ExceedResponse;
import store.novabook.store.member.dto.response.GetMemberAddressResponse;
import store.novabook.store.member.entity.StreetAddress;
import store.novabook.store.member.service.MemberAddressService;

@WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
@AutoConfigureMockMvc
@WebMvcTest(controllers = MemberAddressController.class)
@MockBean(JpaMetamodelMappingContext.class)
class MemberAddressControllerTest {

	@MockBean
	private MemberAddressService memberAddressService;

	@MockBean
	private CurrentMembersArgumentResolver currentMemberResolver;

	@Autowired
	private MockMvc mockMvc;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);

		when(currentMemberResolver.supportsParameter(any())).thenReturn(true);
		when(currentMemberResolver.resolveArgument(any(), any(), any(), any())).thenReturn(1L);
	}

	@Test
	void createMemberAddress() throws Exception {
		CreateMemberAddressRequest request = new CreateMemberAddressRequest("12345", "123 Main St", "Home", "Apt 101");
		CreateMemberAddressResponse response = new CreateMemberAddressResponse(1L,
			new StreetAddress("12345", "123 Main St"), "123");

		when(memberAddressService.createMemberAddress(any(CreateMemberAddressRequest.class), anyLong())).thenReturn(
			response);

		mockMvc.perform(post("/api/v1/store/addresses")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.body.id").value(1L));
	}

	@Test
	void getMemberAddressAll() throws Exception {
		List<GetMemberAddressResponse> memberAddresses = List.of(
			new GetMemberAddressResponse(1L, 1L, 1L, "12345", "Home", "123 Main St", "Apt 101")
		);

		when(memberAddressService.getMemberAddressAll(anyLong())).thenReturn(memberAddresses);

		mockMvc.perform(get("/api/v1/store/addresses")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.body.memberAddresses").isArray())
			.andExpect(jsonPath("$.body.memberAddresses[0].id").value(1L));
	}

	@Test
	void getMemberAddress() throws Exception {
		GetMemberAddressResponse response = new GetMemberAddressResponse(1L, 1L, 1L, "12345", "Home", "123 Main St",
			"Apt 101");

		when(memberAddressService.getMemberAddress(anyLong())).thenReturn(response);

		mockMvc.perform(get("/api/v1/store/addresses/{memberAddressId}", 1L)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.body.id").value(1L));
	}

	@Test
	void updateMemberAddress() throws Exception {
		UpdateMemberAddressRequest request = new UpdateMemberAddressRequest("54321", "456 Broad St", "Work",
			"Suite 303", 1L);

		doNothing().when(memberAddressService).updateMemberAddress(anyLong(), any(UpdateMemberAddressRequest.class));

		mockMvc.perform(put("/api/v1/store/addresses/{memberAddressId}", 1L)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk());
	}

	@Test
	void deleteMemberAddress() throws Exception {
		doNothing().when(memberAddressService).deleteMemberAddress(anyLong());

		mockMvc.perform(delete("/api/v1/store/addresses/{memberAddressId}", 1L)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}

	@Test
	void isExceed() throws Exception {
		ExceedResponse response = new ExceedResponse(true);

		when(memberAddressService.isExceed(anyLong())).thenReturn(response);

		mockMvc.perform(get("/api/v1/store/addresses/is-exceed")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.body.isExceed").value(true));
	}
}
