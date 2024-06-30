package store.novabook.store.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import store.novabook.store.common.exception.EntityNotFoundException;
import store.novabook.store.member.dto.GetMemberGradeResponse;
import store.novabook.store.member.entity.MemberGradeHistory;
import store.novabook.store.member.repository.MemberGradeHistoryRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberGradeHistoryService {

	private final MemberGradeHistoryRepository memberGradeHistoryRepository;

	public GetMemberGradeResponse getMemberGrade(Long memberId) {
		MemberGradeHistory memberGradeHistory = memberGradeHistoryRepository.findByMemberId(memberId)
			.orElseThrow(() -> new EntityNotFoundException(MemberGradeHistory.class, memberId));
		return GetMemberGradeResponse.from(memberGradeHistory);
	}
}
