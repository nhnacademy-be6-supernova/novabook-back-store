package store.novabook.store.member.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import store.novabook.store.member.dto.request.CreateMemberRequest;
import store.novabook.store.member.dto.request.DeleteMemberRequest;
import store.novabook.store.member.dto.request.GetMembersUUIDRequest;
import store.novabook.store.member.dto.request.LoginMemberRequest;
import store.novabook.store.member.dto.request.UpdateMemberPasswordRequest;
import store.novabook.store.member.dto.request.UpdateMemberRequest;
import store.novabook.store.member.dto.response.CreateMemberResponse;
import store.novabook.store.member.dto.response.FindMemberLoginResponse;
import store.novabook.store.member.dto.response.GetMemberResponse;
import store.novabook.store.member.dto.response.GetMembersUUIDResponse;
import store.novabook.store.member.dto.response.LoginMemberResponse;

public interface MemberService {
	CreateMemberResponse createMember(CreateMemberRequest createMemberRequest);

	@Transactional(readOnly = true)
	Page<GetMemberResponse> getMemberAll(Pageable pageable);

	@Transactional(readOnly = true)
	GetMemberResponse getMember(Long memberId);

	void updateMemberNumberOrName(Long memberId, UpdateMemberRequest updateMemberRequest);

	void updateMemberPassword(Long memberId,
		UpdateMemberPasswordRequest updateMemberPasswordRequest);

	void updateMemberStatusToDormant(Long memberId);

	void updateMemberStatusToWithdraw(Long memberId, DeleteMemberRequest deleteMemberRequest);

	LoginMemberResponse matches(LoginMemberRequest loginMemberRequest);

	FindMemberLoginResponse findMemberLogin(String loginId);

	GetMembersUUIDResponse findMembersId(GetMembersUUIDRequest getMembersUUIDRequest);

	boolean isCreatableLoginId(String loginId);

	boolean isCreatableEmail(String email);
}
