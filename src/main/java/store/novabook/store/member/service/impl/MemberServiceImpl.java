package store.novabook.store.member.service.impl;

import java.time.LocalDateTime;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.common.exception.AlreadyExistException;
import store.novabook.store.common.exception.EntityNotFoundException;
import store.novabook.store.member.MemberClient;
import store.novabook.store.member.dto.request.CreateMemberRequest;
import store.novabook.store.member.dto.request.DeleteMemberRequest;
import store.novabook.store.member.dto.request.GetMembersUUIDRequest;
import store.novabook.store.member.dto.request.LoginMemberRequest;
import store.novabook.store.member.dto.request.UpdateMemberNameRequest;
import store.novabook.store.member.dto.request.UpdateMemberNumberRequest;
import store.novabook.store.member.dto.request.UpdateMemberPasswordRequest;
import store.novabook.store.member.dto.response.CreateMemberResponse;
import store.novabook.store.member.dto.response.FindMemberLoginResponse;
import store.novabook.store.member.dto.response.GetMemberResponse;
import store.novabook.store.member.dto.response.GetMembersUUIDResponse;
import store.novabook.store.member.dto.response.LoginMemberResponse;
import store.novabook.store.member.entity.Member;
import store.novabook.store.member.entity.MemberGradeHistory;
import store.novabook.store.member.entity.MemberGradePolicy;
import store.novabook.store.member.entity.MemberStatus;
import store.novabook.store.member.repository.MemberGradeHistoryRepository;
import store.novabook.store.member.repository.MemberGradePolicyRepository;
import store.novabook.store.member.repository.MemberRepository;
import store.novabook.store.member.repository.MemberStatusRepository;
import store.novabook.store.member.service.MemberService;
import store.novabook.store.message.MemberRegistrationMessage;
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
	public static final long ID = 1L;
	public static final String REGISTER_POINT = "회원가입 적립금";
	public static final long POINT_AMOUNT = 5000L;
	public static final String LOGIN_FAIL_MESSAGE = "비밀번호가 일치하지 않습니다.";

	private final MemberRepository memberRepository;
	private final PointHistoryRepository pointHistoryRepository;
	private final PointPolicyRepository pointPolicyRepository;
	private final MemberGradePolicyRepository memberGradePolicyRepository;
	private final MemberStatusRepository memberStatusRepository;
	private final MemberGradeHistoryRepository memberGradeHistoryRepository;

	private final MemberClient memberClient;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	private final RabbitTemplate rabbitTemplate;

	@Value("${rabbitmq.exchange.member}")
	private String memberExchange;

	@Value("${rabbitmq.routing.member}")
	private String memberCreateRoutingKey;

	@Override
	public CreateMemberResponse createMember(CreateMemberRequest createMemberRequest) {
		if (!createMemberRequest.loginPassword().equals(createMemberRequest.loginPasswordConfirm())) {
			throw new BadCredentialsException(LOGIN_FAIL_MESSAGE);
		}

		MemberStatus memberStatus = memberStatusRepository.findByName(STATUS_ACTIVE)
			.orElseThrow(() -> new EntityNotFoundException(MemberStatus.class));

		LocalDateTime birth = LocalDateTime.of(createMemberRequest.birthYear(), createMemberRequest.birthMonth(),
			createMemberRequest.birthDay(), 0, 0);

		String encodedPassword = bCryptPasswordEncoder.encode(createMemberRequest.loginPassword());

		Member member = Member.of(createMemberRequest, memberStatus, birth, encodedPassword);

		if (memberRepository.existsByLoginId(createMemberRequest.loginId())) {
			throw new AlreadyExistException(Member.class);
		}

		Member newMember = memberRepository.save(member);

		MemberGradePolicy memberGradePolicy = memberGradePolicyRepository.findByName(GRADE_COMMON)
			.orElseThrow(() -> new EntityNotFoundException(MemberGradePolicy.class));

		MemberGradeHistory memberGradeHistory = MemberGradeHistory.builder()
			.member(newMember)
			.memberGradePolicy(memberGradePolicy)
			.quarter(LocalDateTime.now())
			.build();
		memberGradeHistoryRepository.save(memberGradeHistory);

		PointPolicy pointPolicy = pointPolicyRepository.findById(ID)
			.orElseThrow(() -> new EntityNotFoundException(PointPolicy.class, ID));

		PointHistory pointHistory = PointHistory.of(pointPolicy, null, newMember, REGISTER_POINT, POINT_AMOUNT);
		pointHistoryRepository.save(pointHistory);

		rabbitTemplate.convertAndSend(memberExchange, memberCreateRoutingKey,
			MemberRegistrationMessage.builder().memberId(newMember.getId()).build());
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
	public GetMemberResponse getMember(Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new EntityNotFoundException(Member.class, memberId));
		return GetMemberResponse.fromEntity(member);
	}

	@Override
	public void updateMemberName(Long memberId, UpdateMemberNameRequest updateMemberNameRequest) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new EntityNotFoundException(Member.class, memberId));
		member.updateName(updateMemberNameRequest.name());
	}

	@Override
	public void updateMemberNumber(Long memberId, UpdateMemberNumberRequest updateMemberNumberRequest) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new EntityNotFoundException(Member.class, memberId));
		member.updateNumber(updateMemberNumberRequest.number());
		memberRepository.save(member);
	}

	@Override
	public void updateMemberPassword(Long memberId, UpdateMemberPasswordRequest updateMemberPasswordRequest) {
		if (!updateMemberPasswordRequest.loginPassword().equals(updateMemberPasswordRequest.loginPasswordConfirm())) {
			throw new BadCredentialsException(LOGIN_FAIL_MESSAGE);
		}

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new EntityNotFoundException(Member.class, memberId));
		String encodedPassword = bCryptPasswordEncoder.encode(updateMemberPasswordRequest.loginPassword());
		member.updateLoginPassword(encodedPassword);
		memberRepository.save(member);
	}

	@Override
	public void updateMemberStatusToDormant(Long memberId) {
		MemberStatus newMemberStatus = memberStatusRepository.findByName(STATUS_DORMANT)
			.orElseThrow(() -> new EntityNotFoundException(MemberStatus.class, memberId));

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new EntityNotFoundException(Member.class, memberId));
		member.updateMemberStatus(newMemberStatus);
		memberRepository.save(member);
	}

	@Override
	public void updateMemberStatusToWithdraw(Long memberId, DeleteMemberRequest deleteMemberRequest) {
		MemberStatus newMemberStatus = memberStatusRepository.findByName(STATUS_WITHDRAW)
			.orElseThrow(() -> new EntityNotFoundException(MemberStatus.class, memberId));

		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new EntityNotFoundException(Member.class, memberId));

		if (!member.getLoginPassword().equals(deleteMemberRequest.loginPassword())) {
			throw new BadCredentialsException(LOGIN_FAIL_MESSAGE);
		}

		member.updateMemberStatus(newMemberStatus);
		memberRepository.save(member);
	}

	@Override
	public LoginMemberResponse matches(LoginMemberRequest loginMemberRequest) {
		Member member = memberRepository.findByLoginIdAndLoginPassword(loginMemberRequest.loginId(),
			loginMemberRequest.loginPassword());
		if (member == null) {
			return new LoginMemberResponse(false, null, null);
		}

		return new LoginMemberResponse(true, member.getId(), member.getName());
	}

	@Override
	public FindMemberLoginResponse findMemberLogin(String loginId) {
		Member member = memberRepository.findByLoginId(loginId);
		if (member == null) {
			throw new EntityNotFoundException(Member.class);
		}
		return new FindMemberLoginResponse(member.getId(), member.getLoginId(), member.getLoginPassword(), "ROLE_USER");
	}

	@Override
	public GetMembersUUIDResponse findMembersId(GetMembersUUIDRequest getMembersUUIDRequest) {

		return memberClient.getMembersId(getMembersUUIDRequest);
	}

	@Override
	public boolean isDuplicateLoginId(String loginId) {
		return memberRepository.existsByLoginId(loginId);
	}

	@Override
	public boolean isDuplicateEmail(String email) {
		return memberRepository.existsByEmail(email);
	}
}

