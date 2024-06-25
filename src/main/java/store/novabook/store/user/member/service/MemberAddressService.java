package store.novabook.store.user.member.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.common.exception.AddressLimitExceededException;
import store.novabook.store.common.exception.EntityNotFoundException;
import store.novabook.store.user.member.dto.CreateMemberAddressRequest;
import store.novabook.store.user.member.dto.CreateMemberAddressResponse;
import store.novabook.store.user.member.dto.GetMemberAddressResponse;
import store.novabook.store.user.member.dto.UpdateMemberAddressRequest;
import store.novabook.store.user.member.entity.Member;
import store.novabook.store.user.member.entity.MemberAddress;
import store.novabook.store.user.member.entity.StreetAddress;
import store.novabook.store.user.member.repository.MemberAddressRepository;
import store.novabook.store.user.member.repository.MemberRepository;
import store.novabook.store.user.member.repository.StreetAddressRepository;

@RequiredArgsConstructor
@Service
@Transactional
public class MemberAddressService {

	public static final int ADDRESS_COUNT = 10;
	private final MemberAddressRepository memberAddressRepository;
	private final StreetAddressRepository streetAddressRepository;
	private final MemberRepository memberRepository;

	public CreateMemberAddressResponse createMemberAddress(CreateMemberAddressRequest createMemberAddressRequest) {
		Member member = memberRepository.findById(createMemberAddressRequest.memberId())
			.orElseThrow(() -> new EntityNotFoundException(MemberAddress.class, createMemberAddressRequest.memberId()));

		validateMemberAddress(member);

		StreetAddress streetAddress = streetAddressRepository.findByZipcodeAndStreetAddress(
			createMemberAddressRequest.zipcode(), createMemberAddressRequest.streetAddress());
		if (streetAddress == null) {
			streetAddress = StreetAddress.builder()
				.zipcode(createMemberAddressRequest.zipcode())
				.streetAddress(createMemberAddressRequest.streetAddress())
				.build();
			streetAddressRepository.save(streetAddress);
		}
		MemberAddress memberAddress = MemberAddress.of(createMemberAddressRequest, member, streetAddress);

		MemberAddress newMemberAddress = memberAddressRepository.save(memberAddress);

		return CreateMemberAddressResponse.fromEntity(newMemberAddress);
	}

	@Transactional(readOnly = true)
	public List<GetMemberAddressResponse> getMemberAddressAll() {
		List<MemberAddress> memberAddressList = memberAddressRepository.findAll();
		return memberAddressList.stream()
			.map(memberAddress -> new GetMemberAddressResponse(
				memberAddress.getId(),
				memberAddress.getStreetAddress().getId(),
				memberAddress.getMember().getId(),
				memberAddress.getNickname(),
				memberAddress.getStreetAddress().getStreetAddress(),
				memberAddress.getMemberAddressDetail()))
			.toList();
	}

	@Transactional(readOnly = true)
	public GetMemberAddressResponse getMemberAddress(Long id) {
		MemberAddress memberAddress = memberAddressRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(MemberAddress.class, id));
		return new GetMemberAddressResponse(
			memberAddress.getId(),
			memberAddress.getStreetAddress().getId(),
			memberAddress.getMember().getId(),
			memberAddress.getNickname(),
			memberAddress.getStreetAddress().getStreetAddress(),
			memberAddress.getMemberAddressDetail()
		);
	}

	public void updateMemberAddress(Long id, UpdateMemberAddressRequest updateMemberAddressRequest) {
		MemberAddress memberAddress = memberAddressRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(MemberAddress.class, id));

		StreetAddress streetAddress = streetAddressRepository.findByZipcodeAndStreetAddress(
			updateMemberAddressRequest.zipcode(),
			updateMemberAddressRequest.streetAddress()
		);

		if (streetAddress == null) {
			streetAddress = StreetAddress.builder()
				.zipcode(updateMemberAddressRequest.zipcode())
				.streetAddress(updateMemberAddressRequest.streetAddress())
				.build();
			streetAddressRepository.save(streetAddress);
		}

		memberAddress.update(
			streetAddress,
			updateMemberAddressRequest.nickname(),
			updateMemberAddressRequest.memberAddressDetail()

		);
		memberAddressRepository.save(memberAddress);
	}

	public void deleteMemberAddress(Long id) {
		MemberAddress memberAddress = memberAddressRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(MemberAddress.class, id));
		memberAddressRepository.delete(memberAddress);
	}

	private void validateMemberAddress(Member member) {
		int currentMemberAddressCount = memberAddressRepository.countByMember(member);
		if (currentMemberAddressCount >= 10) {
			throw new AddressLimitExceededException(member.getId());
		}
	}

	public boolean checkMemberAddressCount(Long memberId) {
		int currentMemberAddress = memberAddressRepository.countByMemberId(memberId);
		return currentMemberAddress >= ADDRESS_COUNT;
	}
}
