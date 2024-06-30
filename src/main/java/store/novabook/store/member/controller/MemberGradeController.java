package store.novabook.store.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import store.novabook.store.member.dto.GetMemberGradeResponse;
import store.novabook.store.member.service.MemberGradeHistoryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/store/members/grade")
public class MemberGradeController {

	private final MemberGradeHistoryService memberGradeHistoryService;
	private static final Long MEMBER_ID = 7L;

	@GetMapping
	public ResponseEntity<GetMemberGradeResponse> getMemberGrade() {
		return ResponseEntity.ok(memberGradeHistoryService.getMemberGrade(MEMBER_ID));
	}
}
