package store.novabook.store.user.member.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import store.novabook.store.user.member.dto.CreateMemberRequest;
import store.novabook.store.user.member.entity.Member;
import store.novabook.store.user.member.entity.Users;
import store.novabook.store.user.member.repository.MemberRepository;
import store.novabook.store.user.member.repository.UsersRepository;

@RequiredArgsConstructor
@Service
@Transactional
public class MemberService {

	private final MemberRepository memberRepository;
	private final UsersRepository usersRepository;

	public Member createMember(CreateMemberRequest createMemberRequest) {
		Users user = Users.builder()
			.type(true)
			.build();

		Member member = Member.builder()
			.users(user)
			.loginId(createMemberRequest.loginId())
			.loginPassword(createMemberRequest.loginPassword())
			.name(createMemberRequest.name())
			.number(createMemberRequest.number())
			.email(createMemberRequest.email())
			.birth(createMemberRequest.birth())
			/*.point(5000)*/
			.latestLoginAt(LocalDateTime.now())
			.build();

			//TODO: 회원가입시 적립되는 5000포인트가 포인트 내역이 남도록 하는 함수 필요 !!!!!!!
		return memberRepository.save(member);
	}

	


}

