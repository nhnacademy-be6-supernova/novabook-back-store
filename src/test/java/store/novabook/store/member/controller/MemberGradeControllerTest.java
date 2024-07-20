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

import store.novabook.store.common.security.aop.CurrentMembersArgumentResolver;
import store.novabook.store.member.dto.response.GetMemberGradeResponse;
import store.novabook.store.member.service.MemberGradeHistoryService;

@WithMockUser
@AutoConfigureMockMvc
@WebMvcTest(MemberGradeController.class)
@MockBean(JpaMetamodelMappingContext.class)
class MemberGradeControllerTest {

	@MockBean
	private MemberGradeHistoryService memberGradeHistoryService;

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
	void getMemberGrade() throws Exception {
		GetMemberGradeResponse response = new GetMemberGradeResponse("Gold");

		when(memberGradeHistoryService.getMemberGrade(any())).thenReturn(response);

		mockMvc.perform(get("/api/v1/store/members/grade")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.body.name").value("Gold"));
	}
}
