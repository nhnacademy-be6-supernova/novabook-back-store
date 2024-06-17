package store.novabook.store.user.member.service;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import store.novabook.store.user.member.repository.MemberRepository;
import store.novabook.store.user.member.repository.UserRepositoy;

@RequiredArgsConstructor
@Service
@Transactional
public class MemberService {

	private final MemberRepository memberRepository;
	private final UserRepositoy userRepositoy;



}
