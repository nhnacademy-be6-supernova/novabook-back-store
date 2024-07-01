package store.novabook.store.member.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import store.novabook.store.member.dto.CreateMemberAddressRequest;
import store.novabook.store.member.dto.CreateMemberAddressResponse;
import store.novabook.store.member.dto.GetMemberAddressResponse;
import store.novabook.store.member.dto.UpdateMemberAddressRequest;
import store.novabook.store.member.entity.Member;

public interface MemberAddressService {
	CreateMemberAddressResponse createMemberAddress(CreateMemberAddressRequest createMemberAddressRequest,
		Long memberId);

	@Transactional(readOnly = true)
	List<GetMemberAddressResponse> getMemberAddressAll(Long memberId);

	@Transactional(readOnly = true)
	GetMemberAddressResponse getMemberAddress(Long id);

	void updateMemberAddress(Long id, UpdateMemberAddressRequest updateMemberAddressRequest);

	void deleteMemberAddress(Long id);

	void validateMemberAddress(Member member);

	boolean isCreatable(Long memberId);
}
