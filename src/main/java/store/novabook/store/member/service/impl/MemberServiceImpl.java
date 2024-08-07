package store.novabook.store.member.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import store.novabook.store.common.adatper.CouponType;
import store.novabook.store.common.exception.BadRequestException;
import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.common.exception.NotFoundException;
import store.novabook.store.common.exception.UnauthorizedException;
import store.novabook.store.common.messaging.CouponSender;
import store.novabook.store.common.messaging.dto.CreateCouponMessage;
import store.novabook.store.member.dto.request.CreateMemberRequest;
import store.novabook.store.member.dto.request.DeleteMemberRequest;
import store.novabook.store.member.dto.request.DoorayAuthCodeRequest;
import store.novabook.store.member.dto.request.GetDormantMembersRequest;
import store.novabook.store.member.dto.request.GetDormantMembersUUIDRequest;
import store.novabook.store.member.dto.request.GetMembersUUIDRequest;
import store.novabook.store.member.dto.request.GetPaycoMembersRequest;
import store.novabook.store.member.dto.request.LinkPaycoMembersRequest;
import store.novabook.store.member.dto.request.LoginMemberRequest;
import store.novabook.store.member.dto.request.UpdateMemberPasswordRequest;
import store.novabook.store.member.dto.request.UpdateMemberRequest;
import store.novabook.store.member.dto.response.CreateMemberResponse;
import store.novabook.store.member.dto.response.DuplicateResponse;
import store.novabook.store.member.dto.response.FindMemberLoginResponse;
import store.novabook.store.member.dto.response.GetDormantMembersResponse;
import store.novabook.store.member.dto.response.GetDormantMembersUUIDResponse;
import store.novabook.store.member.dto.response.GetMemberResponse;
import store.novabook.store.member.dto.response.GetMembersUUIDResponse;
import store.novabook.store.member.dto.response.GetPaycoMembersResponse;
import store.novabook.store.member.dto.response.GetmemberNameResponse;
import store.novabook.store.member.dto.response.LoginMemberResponse;
import store.novabook.store.member.entity.Member;
import store.novabook.store.member.entity.MemberGradeHistory;
import store.novabook.store.member.entity.MemberGradePolicy;
import store.novabook.store.member.entity.MemberStatus;
import store.novabook.store.member.repository.MemberGradeHistoryRepository;
import store.novabook.store.member.repository.MemberGradePolicyRepository;
import store.novabook.store.member.repository.MemberRepository;
import store.novabook.store.member.repository.MemberStatusRepository;
import store.novabook.store.member.service.AuthMembersClient;
import store.novabook.store.member.service.MemberService;
import store.novabook.store.point.entity.PointHistory;
import store.novabook.store.point.entity.PointPolicy;
import store.novabook.store.point.repository.PointHistoryRepository;
import store.novabook.store.point.repository.PointPolicyRepository;

@RequiredArgsConstructor
@Service
@Transactional
public class MemberServiceImpl implements MemberService {

	public static final String GRADE_COMMON = "일반";
	public static final String STATUS_ACTIVE = "활동";
	public static final String STATUS_DORMANT = "휴면";
	public static final String STATUS_WITHDRAW = "탈퇴";
	public static final String REGISTER_POINT = "회원가입 적립금";
	public static final String LOGIN_FAIL_MESSAGE = "비밀번호가 일치하지 않습니다.";
	public static final long AUTH_CODE_EXPIRATION = 3;
	public static final String AUTH_CODE = "authCode:";
	public static final String GET_MEMBER_RESPONSE = "GetMemberResponse";

	private final MemberRepository memberRepository;
	private final PointHistoryRepository pointHistoryRepository;
	private final PointPolicyRepository pointPolicyRepository;
	private final MemberGradePolicyRepository memberGradePolicyRepository;
	private final MemberStatusRepository memberStatusRepository;
	private final MemberGradeHistoryRepository memberGradeHistoryRepository;

	private final AuthMembersClient authMembersClient;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	private final CouponSender couponSender;
	private final CacheManager cacheManager;
	private final RedisTemplate<String, String> redisTemplate;

	private Counter newSignUpCounter;
	private Counter returningUserCounter;

	@Override
	public void bindTo(MeterRegistry meterRegistry) {
		this.newSignUpCounter = meterRegistry.counter("members_new_signups");
		this.returningUserCounter = meterRegistry.counter("members_returning_users");
	}

	@Override
	public CreateMemberResponse createMember(@Valid CreateMemberRequest createMemberRequest) {
		if (!createMemberRequest.loginPassword().equals(createMemberRequest.loginPasswordConfirm())) {
			throw new BadCredentialsException(LOGIN_FAIL_MESSAGE);
		}

		MemberStatus memberStatus = memberStatusRepository.findByName(STATUS_ACTIVE)
			.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_STATUS_NOT_FOUND));

		LocalDateTime birth = LocalDateTime.of(createMemberRequest.birthYear(), createMemberRequest.birthMonth(),
			createMemberRequest.birthDay(), 0, 0);

		String encodedPassword = bCryptPasswordEncoder.encode(createMemberRequest.loginPassword());

		Member member = Member.of(createMemberRequest, memberStatus, birth, encodedPassword, "ROLE_MEMBERS");

		if (memberRepository.existsByLoginId(createMemberRequest.loginId())) {
			throw new BadRequestException(ErrorCode.DUPLICATED_LOGIN_ID);
		}

		Member newMember = memberRepository.save(member);

		MemberGradePolicy memberGradePolicy = memberGradePolicyRepository.findByName(GRADE_COMMON)
			.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_GRADE_POLICY_NOT_FOUND));

		MemberGradeHistory memberGradeHistory = MemberGradeHistory.builder()
			.member(newMember)
			.memberGradePolicy(memberGradePolicy)
			.quarter(LocalDateTime.now())
			.build();
		memberGradeHistoryRepository.save(memberGradeHistory);

		PointPolicy pointPolicy = pointPolicyRepository.findTopByOrderByCreatedAtDesc()
			.orElseThrow(() -> new NotFoundException(ErrorCode.POINT_POLICY_NOT_FOUND));

		PointHistory pointHistory = PointHistory.of(pointPolicy, newMember, REGISTER_POINT,
			pointPolicy.getRegisterPoint());
		pointHistoryRepository.save(pointHistory);

		couponSender.sendToNormalQueue(
			CreateCouponMessage.fromEntity(newMember.getId(), new ArrayList<>(), CouponType.WELCOME, null));

		if (newSignUpCounter != null) {
			newSignUpCounter.increment(); // 카운터 증가
		}

		return CreateMemberResponse.fromEntity(newMember);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<GetMemberResponse> getMemberAll(Pageable pageable) {
		Page<Member> memberList = memberRepository.findAll(pageable);
		Page<GetMemberResponse> memberResponse = memberList.map(GetMemberResponse::fromEntity);

		return new PageImpl<>(memberResponse.getContent(), pageable, memberList.getTotalElements());
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(value = "GetMemberResponse", key = "#memberId")
	public GetMemberResponse getMember(Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
		return GetMemberResponse.fromEntity(member);
	}

	@Override
	public void updateMemberNumberOrName(Long memberId, @Valid UpdateMemberRequest updateMemberRequest) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

		if (updateMemberRequest.name() != null) {
			member.updateName(updateMemberRequest.name());
		}
		if (updateMemberRequest.number() != null) {
			member.updateNumber(updateMemberRequest.number());
		}
		memberRepository.save(member);
		Objects.requireNonNull(cacheManager.getCache(GET_MEMBER_RESPONSE)).evict(member.getId());
		Objects.requireNonNull(cacheManager.getCache("GetMemberName")).evict(member.getId());
	}

	@Override
	public void updateMemberPassword(Long memberId, @Valid UpdateMemberPasswordRequest updateMemberPasswordRequest) {
		if (!updateMemberPasswordRequest.loginPassword().equals(updateMemberPasswordRequest.loginPasswordConfirm())) {
			throw new BadCredentialsException(LOGIN_FAIL_MESSAGE);
		}

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
		String encodedPassword = bCryptPasswordEncoder.encode(updateMemberPasswordRequest.loginPassword());
		member.updateLoginPassword(encodedPassword);
		memberRepository.save(member);
	}

	@Override
	public void updateMemberStatusToDormant(Long memberId) {
		MemberStatus newMemberStatus = memberStatusRepository.findByName(STATUS_DORMANT)
			.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_STATUS_NOT_FOUND));

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
		member.updateMemberStatus(newMemberStatus);
		memberRepository.save(member);

		Objects.requireNonNull(cacheManager.getCache(GET_MEMBER_RESPONSE)).evict(memberId);
	}

	@Override
	public void updateMemberStatusToWithdraw(Long memberId, @Valid DeleteMemberRequest deleteMemberRequest) {
		MemberStatus newMemberStatus = memberStatusRepository.findByName(STATUS_WITHDRAW)
			.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_STATUS_NOT_FOUND));

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

		if (!bCryptPasswordEncoder.matches(deleteMemberRequest.loginPassword(), member.getLoginPassword())) {
			throw new BadCredentialsException(LOGIN_FAIL_MESSAGE);
		}

		member.updateMemberStatus(newMemberStatus);
		memberRepository.save(member);
		Objects.requireNonNull(cacheManager.getCache(GET_MEMBER_RESPONSE)).evict(memberId);
	}

	// 휴면 회원 해지 인증
	@Override
	public void updateMemberStatusToActive(@Valid DoorayAuthCodeRequest request) {

		GetDormantMembersUUIDRequest getDormantMembersUUIDRequest = new GetDormantMembersUUIDRequest(request.uuid());

		GetDormantMembersUUIDResponse getDormantMembersUUIDResponse = authMembersClient.getDormantMembersId(
			getDormantMembersUUIDRequest).getBody();

		Member member = memberRepository.findById(getDormantMembersUUIDResponse.membersId())
			.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
		MemberStatus newMemberStatus = memberStatusRepository.findByName(STATUS_ACTIVE)
			.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_STATUS_NOT_FOUND));

		validateAuthCode(request.uuid(), request.authCode());
		member.updateMemberStatus(newMemberStatus);
		deleteAuthCodeFromRedis(request.uuid());
	}

	@Override
	public LoginMemberResponse matches(LoginMemberRequest loginMemberRequest) {
		Member member = memberRepository.findByLoginIdAndLoginPassword(loginMemberRequest.loginId(),
			loginMemberRequest.loginPassword());
		if (member == null) {
			returningUserCounter.increment();
			return new LoginMemberResponse(false, null, null);
		}

		return new LoginMemberResponse(true, member.getId(), member.getName());
	}

	@Override
	public FindMemberLoginResponse findMembersLogin(String loginId) {
		Member member = memberRepository.findByLoginId(loginId);
		if (member == null) {
			throw new NotFoundException(ErrorCode.MEMBER_NOT_FOUND);
		}
		return new FindMemberLoginResponse(member.getId(), member.getLoginId(), member.getLoginPassword(),
			member.getRole());
	}

	@Override
	public GetPaycoMembersResponse getPaycoMembers(GetPaycoMembersRequest getPaycoMembersRequest) {
		Member member = memberRepository.findByOauthId(getPaycoMembersRequest.paycoId());
		if (member == null) {
			return null;
		}
		return new GetPaycoMembersResponse(member.getId());
	}

	@Override
	public void linkPaycoMembers(LinkPaycoMembersRequest linkPaycoMembersRequest) {
		Long membersId = linkPaycoMembersRequest.membersId();
		Member member = memberRepository.findById(membersId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

		member.updateOauthId(linkPaycoMembersRequest.oauthId());

		memberRepository.save(member);
	}

	@Override
	public GetDormantMembersResponse getDormantMembers(GetDormantMembersRequest getDormantMembersRequest) {
		Member member = memberRepository.findById(getDormantMembersRequest.membersId()).orElseThrow();
		MemberStatus memberStatus = member.getMemberStatus();
		return new GetDormantMembersResponse(memberStatus.getId());
	}

	@Override
	public GetMembersUUIDResponse findMembersId(GetMembersUUIDRequest getMembersUUIDRequest) {

		return authMembersClient.getMembersId(getMembersUUIDRequest).getBody();
	}

	@Override
	public DuplicateResponse isDuplicateLoginId(String loginId) {
		return new DuplicateResponse(memberRepository.existsByLoginId(loginId));
	}

	@Override
	public DuplicateResponse isDuplicateEmail(String email) {
		return new DuplicateResponse(memberRepository.existsByEmail(email));
	}

	// 인증 코드 생성하고 저장하기
	@Override
	public String createAndSaveAuthCode(String uuid) {
		String authCode = UUID.randomUUID().toString().substring(0, 6);
		redisTemplate.opsForValue().set(AUTH_CODE + uuid, authCode, AUTH_CODE_EXPIRATION, TimeUnit.MINUTES);
		return authCode;
	}

	// 유효한 인증코드인지 검사
	private void validateAuthCode(String uuid, String authCode) {
		String saveCode = String.valueOf(redisTemplate.opsForValue().get(AUTH_CODE + uuid));
		if (!authCode.equals(saveCode)) {
			throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_CODE);
		}
	}

	// 레디스에서 인증코드 지우기
	private void deleteAuthCodeFromRedis(String uuid) {
		Boolean result = redisTemplate.delete(AUTH_CODE + uuid);
		if (result == null || !result) {
			throw new NotFoundException(ErrorCode.UNAUTHORIZED_CODE);
		}
	}

	@Override
	public boolean isDormantMember(Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

		return member.getMemberStatus().getName().equals(STATUS_DORMANT);
	}

	@Override
	@Cacheable(value = "GetMemberName", key = "#memberId")
	public GetmemberNameResponse getMemberName(Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
		return new GetmemberNameResponse(member.getName());
	}

}

