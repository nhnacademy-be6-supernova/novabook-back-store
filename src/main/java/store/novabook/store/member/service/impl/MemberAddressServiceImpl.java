package store.novabook.store.member.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.common.exception.EntityNotFoundException;
import store.novabook.store.exception.BadRequestException;
import store.novabook.store.exception.ErrorCode;
import store.novabook.store.member.dto.request.CreateMemberAddressRequest;
import store.novabook.store.member.dto.request.UpdateMemberAddressRequest;
import store.novabook.store.member.dto.response.CreateMemberAddressResponse;
import store.novabook.store.member.dto.response.ExceedResponse;
import store.novabook.store.member.dto.response.GetMemberAddressResponse;
import store.novabook.store.member.entity.Member;
import store.novabook.store.member.entity.MemberAddress;
import store.novabook.store.member.entity.StreetAddress;
import store.novabook.store.member.repository.MemberAddressRepository;
import store.novabook.store.member.repository.MemberRepository;
import store.novabook.store.member.repository.StreetAddressRepository;
import store.novabook.store.member.service.MemberAddressService;

@RequiredArgsConstructor
@Service
@Transactional
public class MemberAddressServiceImpl implements MemberAddressService {

	public static final int ADDRESS_COUNT = 10;
	private final MemberAddressRepository memberAddressRepository;
	private final StreetAddressRepository streetAddressRepository;
	private final MemberRepository memberRepository;

	@Override
	public CreateMemberAddressResponse createMemberAddress(CreateMemberAddressRequest createMemberAddressRequest,
		Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new EntityNotFoundException(Member.class, memberId));

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

	@Override
	@Transactional(readOnly = true)
	public List<GetMemberAddressResponse> getMemberAddressAll(Long memberId) {
		List<MemberAddress> memberAddressList = memberAddressRepository.findAllByMemberId(memberId);
		return memberAddressList.stream()
			.map(memberAddress -> new GetMemberAddressResponse(
				memberAddress.getId(),
				memberAddress.getStreetAddress().getId(),
				memberAddress.getMember().getId(),
				memberAddress.getStreetAddress().getZipcode(),
				memberAddress.getNickname(),
				memberAddress.getStreetAddress().getStreetAddress(),
				memberAddress.getMemberAddressDetail()))
			.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public GetMemberAddressResponse getMemberAddress(Long id) {
		MemberAddress memberAddress = memberAddressRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(MemberAddress.class, id));
		return new GetMemberAddressResponse(
			memberAddress.getId(),
			memberAddress.getStreetAddress().getId(),
			memberAddress.getMember().getId(),
			memberAddress.getStreetAddress().getZipcode(),
			memberAddress.getNickname(),
			memberAddress.getStreetAddress().getStreetAddress(),
			memberAddress.getMemberAddressDetail()
		);
	}

	@Override
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

	@Override
	public void deleteMemberAddress(Long id) {
		MemberAddress memberAddress = memberAddressRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException(MemberAddress.class, id));
		memberAddressRepository.delete(memberAddress);
	}

	@Override
	public void validateMemberAddress(Member member) {
		int currentMemberAddressCount = memberAddressRepository.countByMember(member);
		if (currentMemberAddressCount >= 10) {
			throw new BadRequestException(ErrorCode.LIMITED_ADDRESS_OVER);
			// throw new AddressLimitExceededException(member.getId());
		}
	}

	@Override
	public ExceedResponse isExceed(Long memberId) {
		int currentMemberAddress = memberAddressRepository.countByMemberId(memberId);
		boolean isExceed = currentMemberAddress >= ADDRESS_COUNT;
		return new ExceedResponse(isExceed);
	}
}
