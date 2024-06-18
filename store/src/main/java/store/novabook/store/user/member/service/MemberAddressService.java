package store.novabook.store.user.member.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.user.member.dto.CreateMemberAddressRequest;
import store.novabook.store.user.member.dto.GetMemberAddressResponse;
import store.novabook.store.user.member.entity.Member;
import store.novabook.store.user.member.entity.MemberAddress;
import store.novabook.store.user.member.entity.StreetAddress;
import store.novabook.store.user.member.exception.MemberAddressAlreadyExistException;
import store.novabook.store.user.member.exception.MemberAddressNotFoundException;
import store.novabook.store.user.member.exception.MemberAlreadyExistsException;
import store.novabook.store.user.member.repository.MemberAddressRepository;
import store.novabook.store.user.member.repository.MemberRepository;
import store.novabook.store.user.member.repository.StreetAddressRepository;

@RequiredArgsConstructor
@Service
@Transactional
public class MemberAddressService {

	private final MemberAddressRepository memberAddressRepository;
	private final StreetAddressRepository streetAddressRepository;
	private final MemberRepository memberRepository;

	public MemberAddress createMemberAddress(CreateMemberAddressRequest createMemberAddressRequest) {
		StreetAddress streetAddress = getOrCreateStreetAddress(createMemberAddressRequest);
		Member member = memberRepository.findById(createMemberAddressRequest.memberId())
			.orElseThrow(() -> new MemberAlreadyExistsException(createMemberAddressRequest.memberId()));

		MemberAddress memberAddress = MemberAddress.builder()
			.streetAddress(streetAddress)
			.member(member)
			.nickname(createMemberAddressRequest.nickName())
			.memberAddressDetail(createMemberAddressRequest.memberAddressDetail())
			.build();

		validateMemberAddress(member.getId(), streetAddress.getId());
		return memberAddressRepository.save(memberAddress);
	}

	@Transactional(readOnly = true)
	public List<GetMemberAddressResponse> getMemberAddressAll() {
		List<MemberAddress> memberAddressList = memberAddressRepository.findAll();
		return memberAddressList.stream()
			.map(memberAddress -> new GetMemberAddressResponse(
				memberAddress.getId(),
				memberAddress.getStreetAddress().getId(),
				memberAddress.getId(),
				memberAddress.getNickname(),
				memberAddress.getMemberAddressDetail()))
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public GetMemberAddressResponse getMemberAddress(Long id) {
		MemberAddress memberAddress = memberAddressRepository.findById(id)
			.orElseThrow(() -> new MemberAddressNotFoundException());
		return new GetMemberAddressResponse(
			memberAddress.getId(),
			memberAddress.getStreetAddress().getId(),
			memberAddress.getMember().getId(),
			memberAddress.getNickname(),
			memberAddress.getMemberAddressDetail()
		);
	}

	public MemberAddress updateMemberAddress(Long id, CreateMemberAddressRequest createMemberAddressRequest) {
		MemberAddress memberAddress = memberAddressRepository.findById(id)
			.orElseThrow(() -> new MemberAddressNotFoundException());
		StreetAddress streetAddress = getOrCreateStreetAddress(createMemberAddressRequest);

		memberAddress.update(streetAddress, createMemberAddressRequest.nickName(), createMemberAddressRequest.memberAddressDetail());
		return memberAddressRepository.save(memberAddress);
	}

	public void deleteMemberAddress(Long id) {
		MemberAddress memberAddress = memberAddressRepository.findById(id)
			.orElseThrow(() -> new MemberAddressNotFoundException());
		memberAddressRepository.delete(memberAddress);
	}


	private StreetAddress getOrCreateStreetAddress(CreateMemberAddressRequest createMemberAddressRequest) {
		StreetAddress existingStreetAddress = streetAddressRepository.findByZipcodeAndStreetAddress(
			createMemberAddressRequest.zipcode(),
			createMemberAddressRequest.streetAddress()
		);

		if (existingStreetAddress != null) {
			return existingStreetAddress;
		}

		StreetAddress newStreetAddress = StreetAddress.builder()
			.zipcode(createMemberAddressRequest.zipcode())
			.streetAddress(createMemberAddressRequest.streetAddress())
			.build();

		return streetAddressRepository.save(newStreetAddress);
	}

	private void validateMemberAddress(Long memberId, Long streetAddressId) {
		if (memberAddressRepository.existsByMemberIdAndStreetAddressId(memberId, streetAddressId)) {
			throw new MemberAddressAlreadyExistException(memberId, streetAddressId);
		}
	}

}
