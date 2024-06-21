package store.novabook.store.user.member.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import store.novabook.store.common.exception.AlreadyExistException;
import store.novabook.store.common.exception.EntityNotFoundException;
import store.novabook.store.point.entity.PointHistory;
import store.novabook.store.point.entity.PointPolicy;
import store.novabook.store.point.repository.PointHistoryRepository;
import store.novabook.store.point.repository.PointPolicyRepository;
import store.novabook.store.user.member.dto.CreateMemberRequest;
import store.novabook.store.user.member.dto.CreateMemberResponse;
import store.novabook.store.user.member.dto.GetMemberResponse;
import store.novabook.store.user.member.dto.UpdateMemberRequest;
import store.novabook.store.user.member.entity.Member;
import store.novabook.store.user.member.entity.MemberGrade;
import store.novabook.store.user.member.entity.MemberStatus;
import store.novabook.store.user.member.entity.Users;
import store.novabook.store.user.member.repository.MemberGradeRepository;
import store.novabook.store.user.member.repository.MemberRepository;
import store.novabook.store.user.member.repository.MemberStatusRepository;
import store.novabook.store.user.member.repository.UsersRepository;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

	@Mock
	private MemberRepository memberRepository;

	@Mock
	private UsersRepository usersRepository;

	@Mock
	private PointHistoryRepository pointHistoryRepository;

	@Mock
	private PointPolicyRepository pointPolicyRepository;

	@Mock
	private MemberGradeRepository memberGradeRepository;

	@Mock
	private MemberStatusRepository memberStatusRepository;

	@InjectMocks
	private MemberService memberService;

	private CreateMemberRequest createMemberRequest;
	private Member member;
	private Users user;
	private MemberGrade memberGrade;
	private MemberStatus dormantStatus;
	private MemberStatus withdrawnStatus;
	private MemberStatus memberStatus;
	private PointPolicy pointPolicy;

	@BeforeEach
	void setUp() {
		createMemberRequest = CreateMemberRequest.builder()
			.loginId("testId")
			.loginPassword("testPassword")
			.name("testUser")
			.number("010-1234-5678")
			.email("test@example.com")
			.birthYear(2001)
			.birthMonth(2)
			.birthDay(14)
			.build();

		user = Users.builder().id(MemberService.ID).type(MemberService.TYPE).createdAt(LocalDateTime.now()).build();
		memberGrade = new MemberGrade(1L, MemberService.GRADE_COMMON, 0L, 100000L, 1L, LocalDateTime.now(),
			LocalDateTime.now());
		memberStatus = new MemberStatus(1L, MemberService.STATUS_ACTIVE, LocalDateTime.now(), LocalDateTime.now());
		dormantStatus = new MemberStatus(2L, MemberService.STATUS_DORMANT, LocalDateTime.now(), LocalDateTime.now());
		withdrawnStatus = new MemberStatus(3L, MemberService.STATUS_WITHDRAWN, LocalDateTime.now(),
			LocalDateTime.now());
		pointPolicy = new PointPolicy(1L, 0L, 0L, MemberService.POINT_AMOUNT, LocalDateTime.now(),
			LocalDateTime.now());

		member = Member.builder()
			.id(1L)
			.loginId(createMemberRequest.loginId())
			.loginPassword(createMemberRequest.loginPassword())
			.name(createMemberRequest.name())
			.number(createMemberRequest.number())
			.email(createMemberRequest.email())
			.birth(LocalDateTime.of(2001, 2, 14, 0, 0))
			.point(5000L)
			.totalAmount(0L)
			.latestLoginAt(LocalDateTime.now())
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.memberGrade(memberGrade)
			.memberStatus(memberStatus)
			.users(user)
			.build();
	}

	@Test
	@DisplayName("회원가입 - 성공")
	void createMemberSuccess() {
		when(usersRepository.save(any(Users.class))).thenReturn(user);
		when(memberGradeRepository.findByName(MemberService.GRADE_COMMON)).thenReturn(Optional.of(memberGrade));
		when(memberStatusRepository.findByName(MemberService.STATUS_ACTIVE)).thenReturn(Optional.of(memberStatus));
		when(memberRepository.existsByLoginId(createMemberRequest.loginId())).thenReturn(false);
		when(memberRepository.save(any(Member.class))).thenReturn(member);
		when(pointPolicyRepository.findById(MemberService.ID)).thenReturn(Optional.of(pointPolicy));

		CreateMemberResponse createMemberResponse = memberService.createMember(createMemberRequest);
		assertNotNull(createMemberResponse);
		assertEquals(member.getId(), createMemberResponse.id());

		verify(usersRepository, times(1)).save(any(Users.class));
		verify(memberRepository, times(1)).save(any(Member.class));
		verify(pointPolicyRepository, times(1)).findById(MemberService.ID);
		verify(pointHistoryRepository, times(1)).save(any(PointHistory.class));
	}

	@Test
	@DisplayName("회원가입 - 실패 - 아이디 중복")
	void createMemberFail() {
		when(memberRepository.existsByLoginId(createMemberRequest.loginId())).thenReturn(true);
		when(memberGradeRepository.findByName(any())).thenReturn(Optional.ofNullable(memberGrade));
		when(memberStatusRepository.findByName(any())).thenReturn(Optional.ofNullable(memberStatus));

		assertThrows(AlreadyExistException.class, () -> memberService.createMember(createMemberRequest));
		verify(memberRepository, times(1)).existsByLoginId(createMemberRequest.loginId());
	}

	@Test
	@DisplayName("회원 정보 전체 조회 - 성공")
	void getMemberAll() {
		List<Member> memberList = Collections.singletonList(member);
		Page<Member> page = new PageImpl<>(memberList, PageRequest.of(0, 10), memberList.size());
		when(memberRepository.findAll(any(Pageable.class))).thenReturn(page);
		Page<GetMemberResponse> result = memberService.getMemberAll(PageRequest.of(0, 10));

		assertNotNull(result);
		assertEquals(1, result.getTotalElements());
	}

	@Test
	@DisplayName("회원 조회 - 성공")
	void getMemberSuccess() {
		when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
		GetMemberResponse response = memberService.getMember(1L);

		assertNotNull(response);
		assertEquals(member.getId(), response.id());
		assertEquals(member.getLoginId(), response.loginId());
		assertEquals(member.getName(), response.name());
		assertEquals(member.getEmail(), response.email());

		verify(memberRepository, times(1)).findById(1L);
	}

	@Test
	@DisplayName("회원 조회 - 실패 - 없는 id")
	void getMemberFail() {
		Long notExistId = 2L;
		when(memberRepository.findById(notExistId)).thenReturn(Optional.empty());
		assertThrows(EntityNotFoundException.class, () -> memberService.getMember(notExistId));
	}

	@Test
	@DisplayName("회원 정보 수정 - 성공")
	void updateMember() {
		UpdateMemberRequest updateMemberRequest = new UpdateMemberRequest("newTestLoginPassword", "newTestName",
			"010-1111-1111", "newTestEmail@example.com");
		when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

		memberService.updateMember(1L, updateMemberRequest);

		assertEquals(updateMemberRequest.loginPassword(), member.getLoginPassword());
		assertEquals(updateMemberRequest.name(), member.getName());
		assertEquals(updateMemberRequest.number(), member.getNumber());
		assertEquals(updateMemberRequest.email(), member.getEmail());

		verify(memberRepository, times(1)).findById(1L);
		verify(memberRepository, times(1)).save(any(Member.class));
	}

	@Test
	@DisplayName("회원 상태 휴면으로 수정 - 성공")
	void updateMemberStatusToDormantSuccess() {
		when(memberStatusRepository.findByName(MemberService.STATUS_DORMANT)).thenReturn(Optional.of(dormantStatus));
		when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

		memberService.updateMemberStatusToDormant(1L);

		assertEquals(dormantStatus.getName(), member.getMemberStatus().getName());

		verify(memberStatusRepository, times(1)).findByName(MemberService.STATUS_DORMANT);
		verify(memberRepository, times(1)).findById(1L);
		verify(memberRepository, times(1)).save(any(Member.class));
	}

	@Test
	@DisplayName("회원 상태 휴면으로 수정 - 실패 - 회원 상태 없음")
	void updateMemberStatusToDormantFailNoStatus() {
		when(memberStatusRepository.findByName(MemberService.STATUS_DORMANT)).thenReturn(Optional.empty());

		EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
			() -> memberService.updateMemberStatusToDormant(1L));

		verify(memberStatusRepository, times(1)).findByName(MemberService.STATUS_DORMANT);
		verify(memberRepository, never()).findById(anyLong());
		verify(memberRepository, never()).save(any(Member.class));
	}

	@Test
	@DisplayName("회원 상태 휴면으로 수정 - 실패 - 회원 없음")
	void updateMemberStatusToDormantFailNoMember() {
		when(memberStatusRepository.findByName(MemberService.STATUS_DORMANT)).thenReturn(Optional.of(dormantStatus));
		when(memberRepository.findById(1L)).thenReturn(Optional.empty());

		EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
			() -> memberService.updateMemberStatusToDormant(1L));

		verify(memberStatusRepository, times(1)).findByName(MemberService.STATUS_DORMANT);
		verify(memberRepository, times(1)).findById(1L);
		verify(memberRepository, never()).save(any(Member.class));
	}

	@Test
	@DisplayName("회원 상태 탈퇴로 수정 - 성공")
	void updateMemberStatusToWithdrawnSuccess() {
		when(memberStatusRepository.findByName(MemberService.STATUS_WITHDRAWN)).thenReturn(
			Optional.of(withdrawnStatus));
		when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

		memberService.updateMemberStatusToWithdrawn(1L);

		assertEquals(withdrawnStatus.getName(), member.getMemberStatus().getName());

		verify(memberStatusRepository, times(1)).findByName(MemberService.STATUS_WITHDRAWN);
		verify(memberRepository, times(1)).findById(1L);
		verify(memberRepository, times(1)).save(any(Member.class));
	}

	@Test
	@DisplayName("회원 상태 탈퇴로 수정 - 실패 - 회원 상태 없음")
	void updateMemberStatusToWithdrawnFailNoStatus() {
		when(memberStatusRepository.findByName(MemberService.STATUS_WITHDRAWN)).thenReturn(Optional.empty());

		EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
			() -> memberService.updateMemberStatusToWithdrawn(1L));

		verify(memberStatusRepository, times(1)).findByName(MemberService.STATUS_WITHDRAWN);
		verify(memberRepository, never()).findById(anyLong());
		verify(memberRepository, never()).save(any(Member.class));
	}

	@Test
	@DisplayName("회원 상태 탈퇴로 수정 - 실패 - 회원 없음")
	void updateMemberStatusToWithdrawnFailNoMember() {
		when(memberStatusRepository.findByName(MemberService.STATUS_WITHDRAWN)).thenReturn(
			Optional.of(withdrawnStatus));
		when(memberRepository.findById(1L)).thenReturn(Optional.empty());

		EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
			() -> memberService.updateMemberStatusToWithdrawn(1L));

		verify(memberStatusRepository, times(1)).findByName(MemberService.STATUS_WITHDRAWN);
		verify(memberRepository, times(1)).findById(1L);
		verify(memberRepository, never()).save(any(Member.class));
	}
}