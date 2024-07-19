package store.novabook.store.member.controller;

import static org.mockito.ArgumentMatchers.*;
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

import store.novabook.store.member.dto.request.DoorayAuthCodeRequest;
import store.novabook.store.member.dto.request.DoorayAuthRequest;
import store.novabook.store.member.service.DoorayService;
import store.novabook.store.member.service.MemberService;

@WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
@AutoConfigureMockMvc
@WebMvcTest(controllers = DoorayHookController.class)
@MockBean(JpaMetamodelMappingContext.class)
class DoorayHookControllerTest {

	@MockBean
	private MemberService memberService;

	@MockBean
	private DoorayService doorayService;

	@Autowired
	private MockMvc mockMvc;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void sendMessage() throws Exception {
		DoorayAuthRequest request = new DoorayAuthRequest("test-uuid");

		when(memberService.createAndSaveAuthCode(anyString())).thenReturn("auth-code");
		doNothing().when(doorayService).sendAuthCode(anyString(), anyString());

		mockMvc.perform(post("/api/v1/store/dooray/send-auth-code")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk());
	}

	@Test
	void confirmDormantMember() throws Exception {
		DoorayAuthCodeRequest request = new DoorayAuthCodeRequest("test-uuid", "auth-code");

		doNothing().when(memberService).updateMemberStatusToActive(any(DoorayAuthCodeRequest.class));

		mockMvc.perform(post("/api/v1/store/dooray/confirm")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk());
	}
}
