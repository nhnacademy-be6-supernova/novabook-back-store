package store.novabook.store.member.controller;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import store.novabook.store.member.dto.request.CreateMemberRequest;
import store.novabook.store.member.dto.request.DeleteMemberRequest;
import store.novabook.store.member.dto.request.DuplicateEmailRequest;
import store.novabook.store.member.dto.request.DuplicateLoginIdRequest;
import store.novabook.store.member.dto.request.FindMemberRequest;
import store.novabook.store.member.dto.request.GetDormantMembersRequest;
import store.novabook.store.member.dto.request.GetPaycoMembersRequest;
import store.novabook.store.member.dto.request.LinkPaycoMembersRequest;
import store.novabook.store.member.dto.request.LoginMemberRequest;
import store.novabook.store.member.dto.request.UpdateMemberPasswordRequest;
import store.novabook.store.member.dto.request.UpdateMemberRequest;
import store.novabook.store.member.dto.response.CreateMemberResponse;
import store.novabook.store.member.dto.response.DuplicateResponse;
import store.novabook.store.member.dto.response.FindMemberLoginResponse;
import store.novabook.store.member.dto.response.GetDormantMembersResponse;
import store.novabook.store.member.dto.response.GetMemberResponse;
import store.novabook.store.member.dto.response.GetPaycoMembersResponse;
import store.novabook.store.member.dto.response.GetmemberNameResponse;
import store.novabook.store.member.dto.response.LoginMemberResponse;
import store.novabook.store.member.service.AuthMembersClient;
import store.novabook.store.member.service.MemberService;

@WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
@AutoConfigureMockMvc
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(MemberController.class)
class MemberControllerTest {

	@MockBean
	private MemberService memberService;

	@MockBean
	private AuthMembersClient authMembersClient;

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
	void createMember() throws Exception {
		CreateMemberRequest request = new CreateMemberRequest(
			"testLoginId", "testPassword", "testPassword", "testName",
			"1234567890", "test@example.com", "example.com",
			1990, 1, 1, "123 Test Address"
		);
		CreateMemberResponse response = new CreateMemberResponse(1L);

		when(memberService.createMember(any(CreateMemberRequest.class))).thenReturn(response);

		mockMvc.perform(post("/api/v1/store/members")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.body.id").value(1L));
	}

	@Test
	void checkDuplicateLoginId() throws Exception {
		DuplicateLoginIdRequest request = new DuplicateLoginIdRequest("loginId");
		DuplicateResponse response = new DuplicateResponse(true);

		when(memberService.isDuplicateLoginId(anyString())).thenReturn(response);

		mockMvc.perform(post("/api/v1/store/members/login-id/is-duplicate")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.body.isDuplicate").value(true));
	}

	@Test
	void checkDuplicateEmail() throws Exception {
		DuplicateEmailRequest request = new DuplicateEmailRequest("email@example.com");
		DuplicateResponse response = new DuplicateResponse(true);

		when(memberService.isDuplicateEmail(anyString())).thenReturn(response);

		mockMvc.perform(post("/api/v1/store/members/email/is-duplicate")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.body.isDuplicate").value(true));
	}

	@Test
	void getMember() throws Exception {
		GetMemberResponse response = new GetMemberResponse(1L, "loginId", "name", 1990, 1, 1, "1234567890",
			"test@example.com");

		when(memberService.getMember(anyLong())).thenReturn(response);

		mockMvc.perform(get("/api/v1/store/members/member")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.header("X-Current-Member", "1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.body.id").exists());
	}

	@Test
	void getMemberName() throws Exception {
		GetmemberNameResponse response = new GetmemberNameResponse("username");

		when(memberService.getMemberName(anyLong())).thenReturn(response);

		mockMvc.perform(get("/api/v1/store/members/member/name")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.header("X-Current-Member", "1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.body.memberName").value("username"));
	}

	@Test
	void login() throws Exception {
		LoginMemberRequest request = new LoginMemberRequest("testLoginId", "testPassword");
		LoginMemberResponse response = new LoginMemberResponse(true, 1L, "testName");

		when(memberService.matches(any(LoginMemberRequest.class))).thenReturn(response);

		mockMvc.perform(post("/api/v1/store/members/login")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.body.success").value(true))
			.andExpect(jsonPath("$.body.memberId").value(1L))
			.andExpect(jsonPath("$.body.name").value("testName"));
	}

	@Test
	void updateMember() throws Exception {
		UpdateMemberRequest request = new UpdateMemberRequest("testName", "1234567890");

		doNothing().when(memberService).updateMemberNumberOrName(anyLong(), any(UpdateMemberRequest.class));

		mockMvc.perform(put("/api/v1/store/members/member/update")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.header("X-Current-Member", "1"))
			.andExpect(status().isOk());
	}

	@Test
	void updateMemberPassword() throws Exception {
		UpdateMemberPasswordRequest request = new UpdateMemberPasswordRequest("NewPassword123!", "NewPassword123!");

		doNothing().when(memberService).updateMemberPassword(anyLong(), any(UpdateMemberPasswordRequest.class));

		mockMvc.perform(put("/api/v1/store/members/member/password")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.header("X-Current-Member", "1"))
			.andExpect(status().isOk());
	}

	@Test
	void updateMemberStatusToDormant() throws Exception {
		doNothing().when(memberService).updateMemberStatusToDormant(anyLong());

		mockMvc.perform(put("/api/v1/store/members/member/dormant")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.header("X-Current-Member", "1"))
			.andExpect(status().isOk());
	}

	@Test
	void updateMemberStatusToWithdraw() throws Exception {
		DeleteMemberRequest request = new DeleteMemberRequest("testPassword");

		doNothing().when(memberService).updateMemberStatusToWithdraw(anyLong(), any(DeleteMemberRequest.class));

		mockMvc.perform(put("/api/v1/store/members/member/withdraw")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.header("X-Current-Member", "1"))
			.andExpect(status().isOk());
	}

	@Test
	void find() throws Exception {
		FindMemberRequest request = new FindMemberRequest("testLoginId");
		FindMemberLoginResponse response = new FindMemberLoginResponse(1L, "testLoginId", "testPassword", "ROLE_USER");

		when(memberService.findMembersLogin(anyString())).thenReturn(response);

		mockMvc.perform(post("/api/v1/store/members/find")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.body.membersId").value(1L))
			.andExpect(jsonPath("$.body.loginId").value("testLoginId"));
	}

	@Test
	void getPaycoMembers() throws Exception {
		GetPaycoMembersRequest request = new GetPaycoMembersRequest("paycoId");
		GetPaycoMembersResponse response = new GetPaycoMembersResponse(1L);

		when(memberService.getPaycoMembers(any(GetPaycoMembersRequest.class))).thenReturn(response);

		mockMvc.perform(post("/api/v1/store/members/payco")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.body.id").value(1L));
	}

	@Test
	void linkPayco() throws Exception {
		LinkPaycoMembersRequest request = new LinkPaycoMembersRequest(1L, "oauthId");

		doNothing().when(memberService).linkPaycoMembers(any(LinkPaycoMembersRequest.class));

		mockMvc.perform(post("/api/v1/store/members/payco/link")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.header("X-Current-Member", "1"))
			.andExpect(status().isOk());
	}

	@Test
	void getMemberDormantStatus() throws Exception {
		GetDormantMembersRequest request = new GetDormantMembersRequest(1L);
		GetDormantMembersResponse response = new GetDormantMembersResponse(1L);

		when(memberService.getDormantMembers(any(GetDormantMembersRequest.class))).thenReturn(response);

		mockMvc.perform(post("/api/v1/store/members/status")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.body.memberStatusId").value(1L));
	}
}
