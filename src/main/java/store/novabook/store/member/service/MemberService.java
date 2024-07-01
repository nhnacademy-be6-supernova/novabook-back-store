package store.novabook.store.member.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import store.novabook.store.member.dto.CreateMemberRequest;
import store.novabook.store.member.dto.CreateMemberResponse;
import store.novabook.store.member.dto.DeleteMemberRequest;
import store.novabook.store.member.dto.FindMemberLoginResponse;
import store.novabook.store.member.dto.GetMemberResponse;
import store.novabook.store.member.dto.GetMembersUUIDRequest;
import store.novabook.store.member.dto.GetMembersUUIDResponse;
import store.novabook.store.member.dto.LoginMemberRequest;
import store.novabook.store.member.dto.LoginMemberResponse;
import store.novabook.store.member.dto.UpdateMemberNameRequest;
import store.novabook.store.member.dto.UpdateMemberNumberRequest;
import store.novabook.store.member.dto.UpdateMemberPasswordRequest;

public interface MemberService {
	CreateMemberResponse createMember(CreateMemberRequest createMemberRequest);

	@Transactional(readOnly = true)
	Page<GetMemberResponse> getMemberAll(Pageable pageable);

	@Transactional(readOnly = true)
	GetMemberResponse getMember(Long memberId);

	void updateMemberName(Long memberId, UpdateMemberNameRequest updateMemberNameRequest);

	void updateMemberNumber(Long memberId, UpdateMemberNumberRequest updateMemberNumberRequest);

	void updateMemberPassword(Long memberId,
		UpdateMemberPasswordRequest updateMemberPasswordRequest);

	void updateMemberStatusToDormant(Long memberId);

	void updateMemberStatusToWithdraw(Long memberId, DeleteMemberRequest deleteMemberRequest);

	LoginMemberResponse matches(LoginMemberRequest loginMemberRequest);

	FindMemberLoginResponse findMemberLogin(String loginId);

	GetMembersUUIDResponse findMembersId(GetMembersUUIDRequest getMembersUUIDRequest);

	boolean isDuplicateLoginId(String loginId);

	boolean isDuplicateEmail(String email);
}
