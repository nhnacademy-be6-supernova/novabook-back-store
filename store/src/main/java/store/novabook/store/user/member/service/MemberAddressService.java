package store.novabook.store.user.member.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import store.novabook.store.user.member.repository.MemberAddressRepository;

@RequiredArgsConstructor
@Service
public class MemberAddressService {

	private final MemberAddressRepository memberAddressRepository;


}
