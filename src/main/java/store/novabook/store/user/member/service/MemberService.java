package store.novabook.store.user.member.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import store.novabook.store.point.entity.PointHistory;
import store.novabook.store.point.repository.PointHistoryRepository;
import store.novabook.store.user.member.dto.CreateMemberRequest;
import store.novabook.store.user.member.dto.GetMemberResponse;
import store.novabook.store.user.member.entity.Member;
import store.novabook.store.user.member.entity.Users;
import store.novabook.store.user.member.exception.MemberAlreadyExistsException;
import store.novabook.store.user.member.exception.MemberNotFoundException;
import store.novabook.store.user.member.repository.MemberRepository;
import store.novabook.store.user.member.repository.UsersRepository;

@RequiredArgsConstructor
@Service
public class MemberService {

	private final MemberRepository memberRepository;
	private final UsersRepository usersRepository;
	private final PointHistoryRepository pointHistoryRepository;

	@Transactional
	public Member createMember(CreateMemberRequest createMemberRequest) {
		Users user = Users.builder().type(1).build();
		usersRepository.save(user);

		Member member = Member.builder()
			.users(user)
			.loginId(createMemberRequest.loginId())
			.loginPassword(createMemberRequest.loginPassword())
			.name(createMemberRequest.name())
			.number(createMemberRequest.number())
			.email(createMemberRequest.email())
			.birth(createMemberRequest.birth())
			.latestLoginAt(LocalDateTime.now())
			.build();

		validateId(member.getId());
		Member savedMember = memberRepository.save(member);

		PointHistory pointHistory = new PointHistory(null, null, null, savedMember, "welcome 포인트 적립", 5000,
			LocalDateTime.now(), null);
		pointHistoryRepository.save(pointHistory);

		return savedMember;
	}

	public List<GetMemberResponse> getMemberAll() {
		List<Member> memberList = memberRepository.findAll();
		return memberList.stream()
			.map(member -> new GetMemberResponse(member.getId(), member.getLoginId(), member.getName(),
				member.getEmail()))
			.collect(Collectors.toList());
	}

	public GetMemberResponse getMember(Long memberId) {
		Member member = memberRepository.findById(memberId).orElse(null);
		if (member != null) {
			return new GetMemberResponse(
				member.getId(),
				member.getLoginId(),
				member.getName(),
				member.getEmail());
		}
		throw new MemberNotFoundException();
	}

	@Transactional
	public void updateMember(Long memberId, CreateMemberRequest createMemberRequest) {
		Member member = memberRepository.findById(memberId).orElse(null);
		if (member != null) {
			member.update(createMemberRequest.loginId(), createMemberRequest.loginPassword(),
				createMemberRequest.name(), createMemberRequest.number(), createMemberRequest.email(),
				createMemberRequest.birth());

			memberRepository.save(member);
		}
		throw new MemberNotFoundException();
	}

	@Transactional
	public void deleteMember(Long memberId) {
		Member member = memberRepository.findById(memberId).orElse(null);
		if (member != null) {
			memberRepository.delete(member);
		}
		throw new MemberNotFoundException();
	}

	private void validateId(Long id) {
		if (memberRepository.existsById(id)) {
			throw new MemberAlreadyExistsException(id);
		}
	}

}

