package store.novabook.store.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import store.novabook.store.common.security.aop.CurrentMembers;
import store.novabook.store.member.controller.docs.MemberGradeControllerDocs;
import store.novabook.store.member.dto.response.GetMemberGradeResponse;
import store.novabook.store.member.service.MemberGradeHistoryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/store/members/grade")
public class MemberGradeController implements MemberGradeControllerDocs {

	private final MemberGradeHistoryService memberGradeHistoryService;

	@GetMapping
	public ResponseEntity<GetMemberGradeResponse> getMemberGrade(@CurrentMembers Long memberId) {
		return ResponseEntity.ok(memberGradeHistoryService.getMemberGrade(memberId));
	}
}
