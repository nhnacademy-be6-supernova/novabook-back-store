package store.novabook.store.member.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import store.novabook.store.member.dto.request.CreateMemberAddressRequest;
import store.novabook.store.member.dto.request.UpdateMemberAddressRequest;
import store.novabook.store.member.dto.response.CreateMemberAddressResponse;
import store.novabook.store.member.dto.response.ExceedResponse;
import store.novabook.store.member.dto.response.GetMemberAddressResponse;
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

	ExceedResponse isExceed(Long memberId);
}
