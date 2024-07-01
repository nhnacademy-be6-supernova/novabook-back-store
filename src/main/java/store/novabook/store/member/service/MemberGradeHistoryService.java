package store.novabook.store.member.service;

import store.novabook.store.member.dto.response.GetMemberGradeResponse;

public interface MemberGradeHistoryService {
	GetMemberGradeResponse getMemberGrade(Long memberId);
}
