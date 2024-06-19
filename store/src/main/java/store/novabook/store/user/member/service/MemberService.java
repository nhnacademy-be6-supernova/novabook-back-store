package store.novabook.store.user.member.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.exception.EntityNotFoundException;
import store.novabook.store.point.entity.PointHistory;
import store.novabook.store.point.repository.PointHistoryRepository;
import store.novabook.store.user.member.dto.CreateMemberRequest;
import store.novabook.store.user.member.dto.GetMemberResponse;
import store.novabook.store.user.member.entity.Member;
import store.novabook.store.user.member.entity.MemberGrade;
import store.novabook.store.user.member.entity.MemberStatus;
import store.novabook.store.user.member.entity.Users;
import store.novabook.store.user.member.exception.MemberAlreadyExistsException;
import store.novabook.store.user.member.repository.MemberGradeRepository;
import store.novabook.store.user.member.repository.MemberRepository;
import store.novabook.store.user.member.repository.MemberStatusRepository;
import store.novabook.store.user.member.repository.UsersRepository;

@RequiredArgsConstructor
@Service
@Transactional
public class MemberService {

	public static final String COMMON = "일반";
	public static final String ACTIVE = "활동";

	private final MemberRepository memberRepository;
	private final UsersRepository usersRepository;
	private final PointHistoryRepository pointHistoryRepository;
	private final MemberGradeRepository memberGradeRepository;
	private final MemberStatusRepository memberStatusRepository;

	public Member createMember(CreateMemberRequest createMemberRequest) {

		Users user = Users.builder().type(1).build();
		usersRepository.save(user);

		MemberGrade memberGrade = memberGradeRepository.findByName(COMMON);
		if (memberGrade == null) {
			throw new EntityNotFoundException(memberGrade.getId());
		}
		MemberStatus memberStatus = memberStatusRepository.findByName(ACTIVE);
		if (memberStatus == null) {
			throw new EntityNotFoundException(memberStatus.getId());
		}

		Member member = Member.builder()
			.users(user)
			.memberGrade(memberGrade)
			.memberStatus(memberStatus)
			.loginId(createMemberRequest.loginId())
			.loginPassword(createMemberRequest.loginPassword())
			.name(createMemberRequest.name())
			.number(createMemberRequest.number())
			.email(createMemberRequest.email())
			.birth(createMemberRequest.birth())
			.point(5000L)
			.latestLoginAt(LocalDateTime.now())
			.build();

		/*validateId(member.getId());*/
		if (memberRepository.existsByLoginId(createMemberRequest.loginId())) {
			throw new MemberAlreadyExistsException(member.getId());
		}
		return memberRepository.save(member);

		/*PointHistory pointHistory = new PointHistory(
			null,
			null,
			null,
			savedMember,
			"welcome 포인트 적립",
			5000,
			LocalDateTime.now(),
			null);
		pointHistoryRepository.save(pointHistory);*/

	}

	@Transactional(readOnly = true)
	public List<GetMemberResponse> getMemberAll() {
		List<Member> memberList = memberRepository.findAll();
		return memberList.stream()
			.map(member -> new GetMemberResponse(member.getId(), member.getLoginId(), member.getName(),
				member.getEmail()))
			.toList();
	}

	@Transactional(readOnly = true)
	public GetMemberResponse getMember(Long memberId) {
		Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException(memberId));

		return new GetMemberResponse(
			member.getId(),
			member.getLoginId(),
			member.getName(),
			member.getEmail());

	}

	public void updateMember(Long memberId, CreateMemberRequest createMemberRequest) {
		Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException(memberId));

		member.update(createMemberRequest.loginId(), createMemberRequest.loginPassword(),
			createMemberRequest.name(), createMemberRequest.number(), createMemberRequest.email(),
			createMemberRequest.birth());

		memberRepository.save(member);

	}

	public void deleteMember(Long memberId) {
		Member member = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException(memberId));
		memberRepository.delete(member);
	}

/*	private void validateId(Long id) {
		if (memberRepository.existsById(id)) {
			throw new MemberAlreadyExistsException(id);
		}
	}*/

}

