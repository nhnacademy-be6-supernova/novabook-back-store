/*
package store.novabook.store.user.member.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.exception.EntityNotFoundException;
import store.novabook.store.user.member.dto.CreateMemberAddressRequest;
import store.novabook.store.user.member.dto.GetMemberAddressResponse;
import store.novabook.store.user.member.dto.UpdateMemberAddressRequest;
import store.novabook.store.user.member.dto.UpdateMemberAddressResponse;
import store.novabook.store.user.member.entity.Member;
import store.novabook.store.user.member.entity.MemberAddress;
import store.novabook.store.user.member.entity.StreetAddress;
import store.novabook.store.user.member.exception.MemberAddressAlreadyExistException;
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
			.orElseThrow(() -> new EntityNotFoundException(id));
		return new GetMemberAddressResponse(
			memberAddress.getId(),
			memberAddress.getStreetAddress().getId(),
			memberAddress.getMember().getId(),
			memberAddress.getNickname(),
			memberAddress.getMemberAddressDetail()
		);
	}

	public UpdateMemberAddressResponse updateMemberAddress(Long id, UpdateMemberAddressRequest updateMemberAddressRequest) {
		MemberAddress memberAddress = memberAddressRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(id));
		StreetAddress streetAddress = getOrCreateStreetAddress(updateMemberAddressRequest);

		memberAddress.update(
			updateMemberAddressRequest.streetAddress(),
			updateMemberAddressRequest.nickname(),
			updateMemberAddressRequest.memberAddressDetail()

		);
	}

	public void deleteMemberAddress(Long id) {
		MemberAddress memberAddress = memberAddressRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(id));
		memberAddressRepository.delete(memberAddress);
	}


	private StreetAddress getOrCreateStreetAddress(UpdateMemberAddressRequest updateMemberAddressRequest) {
		StreetAddress existingStreetAddress = streetAddressRepository.findByZipcodeAndStreetAddress(
			updateMemberAddressRequest.zipcode(),
			updateMemberAddressRequest.streetAddress()
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
*/
