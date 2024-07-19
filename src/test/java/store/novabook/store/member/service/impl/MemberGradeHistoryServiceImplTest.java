package store.novabook.store.member.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.common.exception.NotFoundException;
import store.novabook.store.member.dto.response.GetMemberGradeResponse;
import store.novabook.store.member.entity.Member;
import store.novabook.store.member.entity.MemberGradeHistory;
import store.novabook.store.member.entity.MemberGradePolicy;
import store.novabook.store.member.repository.MemberGradeHistoryRepository;

class MemberGradeHistoryServiceImplTest {

	@Mock
	private MemberGradeHistoryRepository memberGradeHistoryRepository;

	@InjectMocks
	private MemberGradeHistoryServiceImpl memberGradeHistoryService;

	private MemberGradeHistory testMemberGradeHistory;
	private Member testMember;
	private MemberGradePolicy testMemberGradePolicy;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		testMember = createSampleMember();
		testMemberGradePolicy = createSampleMemberGradePolicy();
		testMemberGradeHistory = createSampleMemberGradeHistory();
	}

	@Test
	void getMemberGrade_ValidInput_ShouldReturnMemberGrade() {
		Long memberId = 1L;

		when(memberGradeHistoryRepository.findFirstByMemberIdOrderByCreatedAtDesc(memberId))
			.thenReturn(Optional.of(testMemberGradeHistory));

		GetMemberGradeResponse response = memberGradeHistoryService.getMemberGrade(memberId);

		assertNotNull(response);
		assertEquals(testMemberGradeHistory.getMemberGradePolicy().getName(), response.name());
	}

	@Test
	void getMemberGrade_InvalidMemberId_ShouldThrowNotFoundException() {
		Long memberId = 999L;

		when(memberGradeHistoryRepository.findFirstByMemberIdOrderByCreatedAtDesc(memberId))
			.thenReturn(Optional.empty());

		NotFoundException exception = assertThrows(NotFoundException.class, () -> {
			memberGradeHistoryService.getMemberGrade(memberId);
		});

		assertEquals(ErrorCode.MEMBER_GRADE_HISTORY_NOT_FOUND, exception.getErrorCode());
	}

	private Member createSampleMember() {
		return Member.builder()
			.loginId("testUser")
			.loginPassword("testPassword")
			.name("John Doe")
			.number("1234567890")
			.email("john.doe@example.com")
			.birth(LocalDateTime.of(1990, 1, 1, 0, 0))
			.latestLoginAt(LocalDateTime.now())
			.authentication(1)
			.role("ROLE_USER")
			.build();
	}

	private MemberGradePolicy createSampleMemberGradePolicy() {
		return MemberGradePolicy.builder()
			.name("Gold")
			.minRange(1000L)
			.maxRange(5000L)
			.saveRate(10L)
			.build();
	}

	private MemberGradeHistory createSampleMemberGradeHistory() {
		return MemberGradeHistory.builder()
			.member(testMember)
			.memberGradePolicy(testMemberGradePolicy)
			.quarter(LocalDateTime.now())
			.build();
	}
}