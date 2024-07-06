package store.novabook.store.member.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import store.novabook.store.member.dto.request.CreateMemberRequest;
import store.novabook.store.member.dto.request.DeleteMemberRequest;
import store.novabook.store.member.dto.request.GetMembersUUIDRequest;
import store.novabook.store.member.dto.request.GetPaycoMembersRequest;
import store.novabook.store.member.dto.request.LoginMemberRequest;
import store.novabook.store.member.dto.request.UpdateMemberPasswordRequest;
import store.novabook.store.member.dto.request.UpdateMemberRequest;
import store.novabook.store.member.dto.response.CreateMemberResponse;
import store.novabook.store.member.dto.response.DuplicateResponse;
import store.novabook.store.member.dto.response.FindMemberLoginResponse;
import store.novabook.store.member.dto.response.GetMemberResponse;
import store.novabook.store.member.dto.response.GetMembersUUIDResponse;
import store.novabook.store.member.dto.response.GetPaycoMembersResponse;
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

	void updateMemberStatusToActive(Long memberId, String authCode);

	LoginMemberResponse matches(LoginMemberRequest loginMemberRequest);

	FindMemberLoginResponse findMembersLogin(String loginId);

	GetPaycoMembersResponse getPaycoMembers(GetPaycoMembersRequest getPaycoMembersRequest);

	GetMembersUUIDResponse findMembersId(GetMembersUUIDRequest getMembersUUIDRequest);

	DuplicateResponse isDuplicateLoginId(String loginId);

	DuplicateResponse isDuplicateEmail(String email);
}
