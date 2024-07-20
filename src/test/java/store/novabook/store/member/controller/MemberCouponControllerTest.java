package store.novabook.store.member.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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

import store.novabook.store.common.adatper.CouponStatus;
import store.novabook.store.common.adatper.CouponType;
import store.novabook.store.common.adatper.DiscountType;
import store.novabook.store.common.adatper.dto.GetCouponAllResponse;
import store.novabook.store.common.adatper.dto.GetCouponHistoryResponse;
import store.novabook.store.common.adatper.dto.GetCouponResponse;
import store.novabook.store.common.adatper.dto.GetUsedCouponHistoryResponse;
import store.novabook.store.common.security.aop.CurrentMembersArgumentResolver;
import store.novabook.store.member.dto.request.CreateMemberCouponRequest;
import store.novabook.store.member.dto.request.DownloadCouponMessageRequest;
import store.novabook.store.member.dto.request.DownloadCouponRequest;
import store.novabook.store.member.dto.request.RegisterCouponRequest;
import store.novabook.store.member.dto.response.CreateMemberCouponResponse;
import store.novabook.store.member.dto.response.GetCouponIdsResponse;
import store.novabook.store.member.service.MemberCouponService;

@WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
@AutoConfigureMockMvc
@WebMvcTest(controllers = MemberCouponController.class)
@MockBean(JpaMetamodelMappingContext.class)
class MemberCouponControllerTest {

	@MockBean
	private MemberCouponService memberCouponService;

	@Mock
	private CurrentMembersArgumentResolver currentMembersArgumentResolver;

	@Autowired
	private MockMvc mockMvc;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.openMocks(this);

		when(currentMembersArgumentResolver.supportsParameter(any())).thenReturn(true);
		when(currentMembersArgumentResolver.resolveArgument(any(), any(), any(), any())).thenReturn(1L);
	}

	@Test
	void createMemberCoupon() throws Exception {
		CreateMemberCouponRequest request = new CreateMemberCouponRequest(1L);
		CreateMemberCouponResponse response = new CreateMemberCouponResponse(1L);

		when(memberCouponService.createMemberCoupon(any(), any(CreateMemberCouponRequest.class))).thenReturn(
			response);

		mockMvc.perform(post("/api/v1/store/members/coupons")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.body.couponId").value(1L));
	}

	@Test
	void registerMemberCoupon() throws Exception {
		RegisterCouponRequest request = new RegisterCouponRequest(1L);
		CreateMemberCouponResponse response = new CreateMemberCouponResponse(1L);

		when(memberCouponService.registerMemberCoupon(any(), any(RegisterCouponRequest.class))).thenReturn(
			response);

		mockMvc.perform(post("/api/v1/store/members/coupons/register")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.body.couponId").value(1L));
	}

	@Test
	void getMemberCouponHistoryByMemberId() throws Exception {
		GetCouponHistoryResponse historyResponse = GetCouponHistoryResponse.builder()
			.createdAt(LocalDateTime.now())
			.name("CouponName")
			.type(CouponType.GENERAL)
			.status(CouponStatus.UNUSED)
			.discountAmount(1000L)
			.discountType(DiscountType.AMOUNT)
			.build();

		Page<GetCouponHistoryResponse> response = new PageImpl<>(Collections.singletonList(historyResponse));

		when(memberCouponService.getMemberCouponHistory(any(), any(PageRequest.class))).thenReturn(response);

		mockMvc.perform(get("/api/v1/store/members/coupons/history")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").isArray())
			.andExpect(jsonPath("$.data[0].name").value("CouponName"));
	}

	@Test
	void getMemberUsedCouponHistoryByMemberId() throws Exception {
		GetCouponResponse couponResponse = GetCouponResponse.builder()
			.usedAt(LocalDateTime.now())
			.name("CouponName")
			.type(CouponType.GENERAL)
			.status(CouponStatus.USED)
			.discountAmount(1000L)
			.discountType(DiscountType.AMOUNT)
			.build();

		GetUsedCouponHistoryResponse usedHistoryResponse = GetUsedCouponHistoryResponse.fromEntity(couponResponse);
		Page<GetUsedCouponHistoryResponse> response = new PageImpl<>(Collections.singletonList(usedHistoryResponse));

		when(memberCouponService.getMemberUsedCouponHistory(any(), any(PageRequest.class))).thenReturn(response);

		mockMvc.perform(get("/api/v1/store/members/coupons/history/used")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").isArray())
			.andExpect(jsonPath("$.data[0].name").value("CouponName"));
	}

	@Test
	void getMemberCouponByMemberId() throws Exception {
		GetCouponResponse couponResponse = GetCouponResponse.builder().build();
		GetCouponAllResponse response = new GetCouponAllResponse(Collections.singletonList(couponResponse));

		when(memberCouponService.getValidAllByMemberId(any())).thenReturn(response);

		mockMvc.perform(get("/api/v1/store/members/coupons/is-valid")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.body.couponResponseList").isArray());
	}

	@Test
	void getMemberCoupon() throws Exception {
		GetCouponIdsResponse response = new GetCouponIdsResponse(Collections.singletonList(1L));

		when(memberCouponService.getMemberCoupon(any())).thenReturn(response);

		mockMvc.perform(get("/api/v1/store/members/coupons")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.body.couponIds").isArray())
			.andExpect(jsonPath("$.body.couponIds[0]").value(1L));
	}

	@Test
	void downloadCoupon() throws Exception {
		DownloadCouponRequest request = new DownloadCouponRequest(1L);
		CreateMemberCouponResponse response = new CreateMemberCouponResponse(1L);

		when(memberCouponService.downloadCoupon(any(), any(DownloadCouponRequest.class))).thenReturn(response);

		mockMvc.perform(post("/api/v1/store/members/coupons/download")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.body.couponId").value(1L));
	}

	@Test
	void downloadLimitedCoupon() throws Exception {
		DownloadCouponMessageRequest request = DownloadCouponMessageRequest.builder()
			.uuid("uuid")
			.couponType(CouponType.GENERAL)
			.couponTemplateId(1L)
			.build();

		doNothing().when(memberCouponService)
			.downloadLimitedCoupon(anyString(), anyString(), anyLong(), any(DownloadCouponMessageRequest.class));

		mockMvc.perform(post("/api/v1/store/members/coupons/download/limited")
				.with(csrf())
				.header("Authorization", "Bearer token")
				.header("Refresh", "refreshToken")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk());
	}
}
