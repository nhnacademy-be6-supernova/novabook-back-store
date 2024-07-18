package store.novabook.store.member.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static store.novabook.store.member.service.impl.MemberServiceImpl.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import store.novabook.store.common.exception.BadRequestException;
import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.common.exception.NotFoundException;
import store.novabook.store.common.messaging.CouponSender;
import store.novabook.store.common.response.ApiResponse;
import store.novabook.store.member.dto.request.CreateMemberRequest;
import store.novabook.store.member.dto.request.DeleteMemberRequest;
import store.novabook.store.member.dto.request.GetDormantMembersRequest;
import store.novabook.store.member.dto.request.GetMembersUUIDRequest;
import store.novabook.store.member.dto.request.GetPaycoMembersRequest;
import store.novabook.store.member.dto.request.LinkPaycoMembersRequest;
import store.novabook.store.member.dto.request.LoginMemberRequest;
import store.novabook.store.member.dto.request.UpdateMemberPasswordRequest;
import store.novabook.store.member.dto.request.UpdateMemberRequest;
import store.novabook.store.member.dto.response.CreateMemberResponse;
import store.novabook.store.member.dto.response.DuplicateResponse;
import store.novabook.store.member.dto.response.GetDormantMembersResponse;
import store.novabook.store.member.dto.response.GetMemberResponse;
import store.novabook.store.member.dto.response.GetMembersUUIDResponse;
import store.novabook.store.member.dto.response.GetPaycoMembersResponse;
import store.novabook.store.member.dto.response.GetmemberNameResponse;
import store.novabook.store.member.dto.response.LoginMemberResponse;
import store.novabook.store.member.entity.Member;
import store.novabook.store.member.entity.MemberGradePolicy;
import store.novabook.store.member.entity.MemberStatus;
import store.novabook.store.member.repository.MemberGradeHistoryRepository;
import store.novabook.store.member.repository.MemberGradePolicyRepository;
import store.novabook.store.member.repository.MemberRepository;
import store.novabook.store.member.repository.MemberStatusRepository;
import store.novabook.store.member.service.AuthMembersClient;
import store.novabook.store.point.entity.PointPolicy;
import store.novabook.store.point.repository.PointHistoryRepository;
import store.novabook.store.point.repository.PointPolicyRepository;

class MemberServiceImplTest {

	@Mock
	private MemberRepository memberRepository;

	@Mock
	private PointHistoryRepository pointHistoryRepository;

	@Mock
	private PointPolicyRepository pointPolicyRepository;

	@Mock
	private MemberGradePolicyRepository memberGradePolicyRepository;

	@Mock
	private MemberStatusRepository memberStatusRepository;

	@Mock
	private MemberGradeHistoryRepository memberGradeHistoryRepository;

	@Mock
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Mock
	private CouponSender couponSender;

	@Mock
	private CacheManager cacheManager;

	@Mock
	private RedisTemplate<String, String> redisTemplate;

	@Mock
	private AuthMembersClient authMembersClient;

	@InjectMocks
	private MemberServiceImpl memberService;

	@Spy
	private Member testMember = new Member("testLoginId", "encodedPassword", "OldName", "OldNumber",
		"testEmail", LocalDateTime.of(1990, 1, 1, 0, 0), LocalDateTime.now(), 1, new MemberStatus("활동"),
		"ROLE_MEMBERS");

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		MemberStatus memberStatus = new MemberStatus("활동");
		when(memberStatusRepository.findByName("활동")).thenReturn(Optional.of(memberStatus));

		MemberGradePolicy memberGradePolicy = new MemberGradePolicy("일반", 0L, 50L, 1L);
		when(memberGradePolicyRepository.findByName("일반")).thenReturn(Optional.of(memberGradePolicy));

		PointPolicy pointPolicy = new PointPolicy(1L, 100, 5000);
		when(pointPolicyRepository.findTopByOrderByCreatedAtDesc()).thenReturn(Optional.of(pointPolicy));

		when(bCryptPasswordEncoder.encode(any())).thenReturn("encodedPassword");

		testMember = new Member("testLoginId", "encodedPassword", "testName", "testNumber",
			"testEmail", LocalDateTime.of(1990, 1, 1, 0, 0),
			LocalDateTime.now(), 1, memberStatus, "ROLE_MEMBERS");

		when(memberRepository.existsByLoginId("testLoginId")).thenReturn(false);
		when(memberRepository.save(any(Member.class))).thenReturn(testMember);

		// Mocking cache manager
		Cache mockCache = mock(Cache.class);
		when(cacheManager.getCache("GetMemberResponse")).thenReturn(mockCache);
		when(cacheManager.getCache("GetMemberName")).thenReturn(mockCache);

	}

	@Test
	@DisplayName("회원 가입 - 성공")
	void createMemberSuccess() {
		CreateMemberRequest request = new CreateMemberRequest(
			"testLoginId", "testPassword", "testPassword", "testName", "testNumber",
			"testEmail", "testDomain", 1990, 1, 1, "testAddress");

		CreateMemberResponse response = memberService.createMember(request);

		assertNotNull(response);
		assertEquals(testMember.getId(), response.id());
	}

	@Test
	@DisplayName("회원 가입 - 실패 - 비밀번호 불일치")
	void createMember_passwordMismatch() {
		CreateMemberRequest request = new CreateMemberRequest("testLoginId", "password", "differentPassword",
			"testName", "testNumber", "testEmail", "testDomain", 1990, 1, 1, "testAddress");

		assertThrows(BadCredentialsException.class, () -> memberService.createMember(request));
	}

	@Test
	@DisplayName("회원 가입 - 실패 - 로그인 아이디 중복")
	void createMemberWithDuplicateLoginId() {
		CreateMemberRequest request = new CreateMemberRequest("existingLoginId", "password", "password", "testName",
			"testNumber", "testEmail", "testDomain", 1990, 1, 1, "testAddress");

		when(memberRepository.existsByLoginId("existingLoginId")).thenReturn(true);

		BadRequestException exception = assertThrows(BadRequestException.class, () -> {
			memberService.createMember(request);
		});

		assertEquals(ErrorCode.DUPLICATED_LOGIN_ID, exception.getErrorCode());
		verify(memberRepository, never()).save(any(Member.class));
	}

	@Test
	@DisplayName("회원 전체 조회 - 성공")
	void getMemberAllSuccess() {
	}

	@Test
	@DisplayName("회원 단건 조회 - 성공")
	void getMember() {
		Long memberId = 1L;
		Member member = new Member("testLoginId", "encodedPassword", "testName", "testNumber",
			"testEmail", LocalDateTime.of(1990, 1, 1, 0, 0),
			LocalDateTime.now(), 1, new MemberStatus("활동"), "ROLE_MEMBERS");
		when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

		GetMemberResponse response = memberService.getMember(memberId);

		assertNotNull(response);
		assertEquals(member.getId(), response.id());

		verify(memberRepository, times(1)).findById(memberId);
	}

	@Test
	@DisplayName("회원 단건 조회 - 실패 - 회원을 찾을 수 없음")
	void getMember_notFound() {
		Long memberId = 1L;
		when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

		NotFoundException exception = assertThrows(NotFoundException.class, () -> {
			memberService.getMember(memberId);
		});

		assertEquals(ErrorCode.MEMBER_NOT_FOUND, exception.getErrorCode());
		verify(memberRepository, times(1)).findById(memberId);
	}

	@Test
	@DisplayName("회원 이름 변경 - 성공")
	void updateMemberNameSuccess() {
	}

	@Test
	@DisplayName("회원 번호 변경 - 성공")
	void updateMemberNumberSuccess() {
	}

	@Test
	@DisplayName("회원 이름과 번호 모두 변경 - 성공")
	void updateMemberNameAndNumberSuccess() {
	}

	@Test
	@DisplayName("회원 이름 변경 - 실패 - 회원을 찾을 수 없음")
	void updateMemberName_notFound() {
		// given
		Long memberId = 1L;
		String newName = "NewName";
		UpdateMemberRequest request = new UpdateMemberRequest(newName, null);

		// mock 설정
		when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

		// when, then
		NotFoundException exception = assertThrows(NotFoundException.class,
			() -> memberService.updateMemberNumberOrName(memberId, request));

		assertEquals(ErrorCode.MEMBER_NOT_FOUND, exception.getErrorCode());
		verify(memberRepository, times(1)).findById(memberId);
		verify(memberRepository, never()).save(any(Member.class));
		verifyNoInteractions(cacheManager);
	}

	@Test
	@DisplayName("회원 비밀번호 변경 - 성공")
	void updateMemberPasswordSuccess() {
		Long memberId = 1L;
		Member member = new Member("testLoginId", "encodedPassword", "testName", "testNumber",
			"testEmail", LocalDateTime.of(1990, 1, 1, 0, 0),
			LocalDateTime.now(), 1, new MemberStatus("활동"), "ROLE_MEMBERS");
		when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
		when(bCryptPasswordEncoder.matches(any(), any())).thenReturn(true);

		UpdateMemberPasswordRequest request = new UpdateMemberPasswordRequest(
			"newPassword", "newPassword");

		memberService.updateMemberPassword(memberId, request);

		verify(memberRepository, times(1)).save(member);
		assertEquals("encodedPassword", member.getLoginPassword());
	}

	@Test
	@DisplayName("회원 비밀번호 변경 - 실패 - 회원을 찾을 수 없음")
	void updateMemberPassword_memberNotFound() {
		Long memberId = 1L;
		when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

		UpdateMemberPasswordRequest request = new UpdateMemberPasswordRequest(
			"newPassword", "newPassword");

		NotFoundException exception = assertThrows(NotFoundException.class, () -> {
			memberService.updateMemberPassword(memberId, request);
		});

		assertEquals(ErrorCode.MEMBER_NOT_FOUND, exception.getErrorCode());
	}

	@Test
	@DisplayName("회원 비밀번호 변경 - 실패 - 새 비밀번호 불일치")
	void updateMemberPassword_passwordMismatch() {
		Long memberId = 1L;
		UpdateMemberPasswordRequest request = new UpdateMemberPasswordRequest(
			"newPassword", "newPasswordDifferent");

		BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> {
			memberService.updateMemberPassword(memberId, request);
		});

		assertEquals("비밀번호가 일치하지 않습니다.", exception.getMessage());
	}

	@Test
	@DisplayName("회원 상태 탈퇴로 변경")
	void updateMemberStatusToWithdraw() {
		Long memberId = 1L;
		DeleteMemberRequest deleteRequest = new DeleteMemberRequest("password");

		MemberStatus activeStatus = new MemberStatus("활동");
		MemberStatus withdrawStatus = new MemberStatus("탈퇴");

		when(memberStatusRepository.findByName("활동")).thenReturn(Optional.of(activeStatus));
		when(memberStatusRepository.findByName("탈퇴")).thenReturn(Optional.of(withdrawStatus));

		when(memberRepository.findById(memberId)).thenReturn(Optional.of(testMember));

		when(bCryptPasswordEncoder.matches(deleteRequest.loginPassword(), testMember.getLoginPassword())).thenReturn(
			true);

		when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));

		Cache cache = mock(Cache.class);
		when(cacheManager.getCache("GetMemberResponse")).thenReturn(cache);
		when(cacheManager.getCache("GetMemberName")).thenReturn(cache);

		memberService.updateMemberStatusToWithdraw(memberId, deleteRequest);

		assertEquals("탈퇴", testMember.getMemberStatus().getName());
		verify(memberRepository, times(1)).findById(memberId);
		verify(memberRepository, times(1)).save(testMember);
		verify(cacheManager.getCache("GetMemberResponse"), times(1)).evict(
			memberId);
		verify(cacheManager.getCache("GetMemberName"), times(1)).evict(memberId);
	}

	@Test
	@DisplayName("회원 탈퇴 - 실패 - 회원을 찾을 수 없음")
	void deleteMember_memberNotFound() {
		Long memberId = 1L;
		when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

		DeleteMemberRequest request = new DeleteMemberRequest("encodedPassword");

		NotFoundException exception = assertThrows(NotFoundException.class, () -> {
			memberService.updateMemberStatusToWithdraw(memberId, request);
		});

		assertEquals(ErrorCode.MEMBER_STATUS_NOT_FOUND, exception.getErrorCode());
	}

	@Test
	@DisplayName("회원 상태 변경 - 휴면 회원으로 변경")
	void updateMemberStatusToDormantSuccess() {
		Long memberId = 1L;

		MemberStatus activeStatus = new MemberStatus("활동");
		MemberStatus dormantStatus = new MemberStatus("휴면");

		when(memberStatusRepository.findByName("활동")).thenReturn(Optional.of(activeStatus));
		when(memberStatusRepository.findByName("휴면")).thenReturn(Optional.of(dormantStatus));

		when(memberRepository.findById(memberId)).thenReturn(Optional.of(testMember));
		when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));

		Cache cache = mock(Cache.class);
		when(cacheManager.getCache("GetMemberResponse")).thenReturn(cache);
		when(cacheManager.getCache("GetMemberName")).thenReturn(cache);

		memberService.updateMemberStatusToDormant(memberId);

		assertEquals(dormantStatus, testMember.getMemberStatus());
		verify(memberRepository, times(1)).findById(memberId);
		verify(memberRepository, times(1)).save(any(Member.class));
		verify(cacheManager.getCache("GetMemberResponse")).evict(memberId);
		verify(cacheManager.getCache("GetMemberName")).evict(memberId);
	}

	@Test
	@DisplayName("휴면 회원 해지 인증 및 상태 변경 - 성공")
	void updateMemberStatusToActive_Success() {

	}

	@Test
	@DisplayName("휴면 회원 해지 인증 및 상태 변경 - 인증 코드 불일치")
	void updateMemberStatusToActive_Unauthorized() {

	}

	@Test
	@DisplayName("휴면 회원 해지 인증 및 상태 변경 - 회원을 찾을 수 없음")
	void updateMemberStatusToActive_MemberNotFound() {

	}

	@Test
	@DisplayName("휴면 회원 해지 인증 및 상태 변경 - 회원 상태를 찾을 수 없음")
	void updateMemberStatusToActive_StatusNotFound() {

	}

	@Test
	@DisplayName("인증 코드 생성 및 저장 - 성공")
	void createAndSaveAuthCode_Success() {
	}

	@Test
	@DisplayName("로그인 멤버 매칭 - 로그인 성공")
	void matchesLoginSuccess() {
		String loginId = "testLoginId";
		String password = "password";

		when(memberRepository.findByLoginIdAndLoginPassword(loginId, password)).thenReturn(testMember);

		LoginMemberRequest request = new LoginMemberRequest(loginId, password);
		LoginMemberResponse response = memberService.matches(request);

		assertTrue(response.success());
		assertEquals(testMember.getId(), response.memberId());
		assertEquals(testMember.getName(), response.name());

		verify(memberRepository, times(1)).findByLoginIdAndLoginPassword(loginId, password);
	}

	@Test
	@DisplayName("로그인 멤버 매칭 - 로그인 실패")
	void matchesLoginFailure() {
		String loginId = "testLoginId";
		String password = "falsePassword";

		when(memberRepository.findByLoginIdAndLoginPassword(loginId, password)).thenReturn(testMember);

		LoginMemberRequest request = new LoginMemberRequest(loginId, password);
		LoginMemberResponse response = memberService.matches(request);

		assertTrue(response.success());
		assertEquals(testMember.getId(), response.memberId());
		assertEquals(testMember.getName(), response.name());

		verify(memberRepository, times(1)).findByLoginIdAndLoginPassword(loginId, password);
	}

	@Test
	@DisplayName("멤버 로그인 정보 조회 - 존재하는 로그인 ID")
	void findMembersLoginExist() {
	}

	@Test
	@DisplayName("멤버 로그인 정보 조회 - 존재하지 않는 로그인 ID")
	void findMembersLoginNotExist() {
		String loginId = "nonExistingLoginId";

		when(memberRepository.findByLoginId(loginId)).thenReturn(null);

		assertThrows(NotFoundException.class, () -> memberService.findMembersLogin(loginId));

		verify(memberRepository, times(1)).findByLoginId(loginId);
	}

	@Test
	@DisplayName("Payco 멤버 정보 조회 - 존재하는 Payco ID")
	void getPaycoMembersExist() {

	}

	@Test
	@DisplayName("Payco 멤버 정보 조회 - 존재하지 않는 Payco ID")
	void getPaycoMembersNotExist() {
		String paycoId = "nonExistingPaycoId";

		when(memberRepository.findByOauthId(paycoId)).thenReturn(null);

		GetPaycoMembersRequest request = new GetPaycoMembersRequest(paycoId);
		GetPaycoMembersResponse response = memberService.getPaycoMembers(request);

		assertNull(response);

		verify(memberRepository, times(1)).findByOauthId(paycoId);
	}

	@Test
	@DisplayName("Payco 멤버 링크")
	void linkPaycoMembers() {
		Long memberId = 1L;
		String paycoId = "testPaycoId";

		when(memberRepository.findById(memberId)).thenReturn(Optional.of(testMember));
		when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));

		LinkPaycoMembersRequest request = new LinkPaycoMembersRequest(memberId, paycoId);
		memberService.linkPaycoMembers(request);

		assertEquals(paycoId, testMember.getOauthId());

		verify(memberRepository, times(1)).findById(memberId);
		verify(memberRepository, times(1)).save(testMember);
	}

	@Test
	@DisplayName("휴면 멤버 상태 조회")
	void getDormantMembers() {
		Long memberId = 1L;
		MemberStatus dormantStatus = new MemberStatus("휴면");

		when(memberRepository.findById(memberId)).thenReturn(Optional.of(testMember));

		GetDormantMembersRequest request = new GetDormantMembersRequest(memberId);
		GetDormantMembersResponse response = memberService.getDormantMembers(request);

		assertNotNull(response);
		assertEquals(dormantStatus.getId(), response.memberStatusId());

		verify(memberRepository, times(1)).findById(memberId);
	}

	@Test
	@DisplayName("멤버 ID 조회 - 성공")
	void findMembersId_Success() {
		// Given
		String uuid = "testUUID";
		GetMembersUUIDRequest request = new GetMembersUUIDRequest(uuid);
		GetMembersUUIDResponse expectedResponse = new GetMembersUUIDResponse(1L, "ROLE_MEMBER");
		ApiResponse<GetMembersUUIDResponse> apiResponse = new ApiResponse<>("Success", true, expectedResponse);

		// Mocking behavior of authMembersClient
		when(authMembersClient.getMembersId(any()))
			.thenReturn(apiResponse);

		// When
		GetMembersUUIDResponse actualResponse = memberService.findMembersId(request);

		// Then
		assertEquals(expectedResponse, actualResponse);
	}

	@Test
	@DisplayName("중복 로그인 ID 확인 - 중복 없음")
	void isDuplicateLoginId_NoDuplicate() {
		// Given
		String loginId = "testLoginId";

		// Mocking memberRepository.existsByLoginId(...)
		when(memberRepository.existsByLoginId(loginId)).thenReturn(false);

		// When
		DuplicateResponse response = memberService.isDuplicateLoginId(loginId);

		// Then
		assertFalse(response.isDuplicate());
		verify(memberRepository, times(1)).existsByLoginId(loginId);
	}

	@Test
	@DisplayName("중복 로그인 ID 확인 - 중복 있음")
	void isDuplicateLoginId_WithDuplicate() {
		// Given
		String loginId = "existingLoginId";

		// Mocking memberRepository.existsByLoginId(...)
		when(memberRepository.existsByLoginId(loginId)).thenReturn(true);

		// When
		DuplicateResponse response = memberService.isDuplicateLoginId(loginId);

		// Then
		assertTrue(response.isDuplicate());
		verify(memberRepository, times(1)).existsByLoginId(loginId);
	}

	@Test
	@DisplayName("중복 이메일 확인 - 중복 없음")
	void isDuplicateEmail_NoDuplicate() {
		// Given
		String email = "test@example.com";

		// Mocking memberRepository.existsByEmail(...)
		when(memberRepository.existsByEmail(email)).thenReturn(false);

		// When
		DuplicateResponse response = memberService.isDuplicateEmail(email);

		// Then
		assertFalse(response.isDuplicate());
		verify(memberRepository, times(1)).existsByEmail(email);
	}

	@Test
	@DisplayName("중복 이메일 확인 - 중복 있음")
	void isDuplicateEmail_WithDuplicate() {
		// Given
		String email = "existing@example.com";

		// Mocking memberRepository.existsByEmail(...)
		when(memberRepository.existsByEmail(email)).thenReturn(true);

		// When
		DuplicateResponse response = memberService.isDuplicateEmail(email);

		// Then
		assertTrue(response.isDuplicate());
		verify(memberRepository, times(1)).existsByEmail(email);
	}

	@Test
	@DisplayName("휴면 회원 여부 확인 - 존재하는 회원")
	void isDormantMember_ExistingMember() {
		// Given
		Long memberId = 1L;
		Member dormantMember = new Member("testLoginId", "encodedPassword", "testName", "testNumber",
			"testEmail", LocalDateTime.of(1990, 1, 1, 0, 0),
			LocalDateTime.now(), 1, new MemberStatus(STATUS_DORMANT), "ROLE_MEMBERS");

		// Mocking memberRepository behavior
		when(memberRepository.findById(memberId))
			.thenReturn(Optional.of(dormantMember));

		// When
		boolean result = memberService.isDormantMember(memberId);

		// Then
		assertTrue(result);
		verify(memberRepository, times(1)).findById(memberId);
	}

	@Test
	@DisplayName("휴면 회원 여부 확인 - 존재하지 않는 회원")
	void isDormantMember_NonExistingMember() {
		// Given
		Long memberId = 999L;

		// Mocking memberRepository behavior
		when(memberRepository.findById(memberId))
			.thenReturn(Optional.empty());

		// When & Then
		assertThrows(NotFoundException.class, () -> memberService.isDormantMember(memberId));
		verify(memberRepository, times(1)).findById(memberId);
	}

	@Test
	@DisplayName("회원 이름 조회 - 존재하는 회원")
	void getMemberName_ExistingMember() {
		// Given
		Long memberId = 1L;
		String memberName = "testName";
		Member member = new Member("testLoginId", "encodedPassword", memberName, "testNumber",
			"testEmail", LocalDateTime.of(1990, 1, 1, 0, 0),
			LocalDateTime.now(), 1, new MemberStatus(STATUS_ACTIVE), "ROLE_MEMBERS");

		// Mocking memberRepository behavior
		when(memberRepository.findById(memberId))
			.thenReturn(Optional.of(member));

		// When
		GetmemberNameResponse response = memberService.getMemberName(memberId);

		// Then
		assertNotNull(response);
		assertEquals(memberName, response.memberName());
		verify(memberRepository, times(1)).findById(memberId);
	}

	@Test
	@DisplayName("회원 이름 조회 - 존재하지 않는 회원")
	void getMemberName_NonExistingMember() {
		// Given
		Long memberId = 999L;

		// Mocking memberRepository behavior
		when(memberRepository.findById(memberId))
			.thenReturn(Optional.empty());

		// When & Then
		assertThrows(NotFoundException.class, () -> memberService.getMemberName(memberId));
		verify(memberRepository, times(1)).findById(memberId);
	}

}