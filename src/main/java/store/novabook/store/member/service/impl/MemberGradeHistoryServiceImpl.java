package store.novabook.store.member.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.common.exception.ErrorCode;
import store.novabook.store.common.exception.NotFoundException;
import store.novabook.store.member.dto.response.GetMemberGradeResponse;
import store.novabook.store.member.entity.MemberGradeHistory;
import store.novabook.store.member.repository.MemberGradeHistoryRepository;
import store.novabook.store.member.service.MemberGradeHistoryService;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberGradeHistoryServiceImpl implements MemberGradeHistoryService {

	private final MemberGradeHistoryRepository memberGradeHistoryRepository;

	@Override
	public GetMemberGradeResponse getMemberGrade(Long memberId) {
		MemberGradeHistory memberGradeHistory = memberGradeHistoryRepository.findFirstByMemberIdOrderByCreatedAtDesc(
			memberId).orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_GRADE_HISTORY_NOT_FOUND));
		return GetMemberGradeResponse.from(memberGradeHistory);
	}
}
