package store.novabook.store.user.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import store.novabook.store.user.member.dto.GetMemberGradeResponse;
import store.novabook.store.user.member.service.MemberGradeHistoryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/store/members/grade")
public class MemberGradeController {

	private final MemberGradeHistoryService memberGradeHistoryService;

	@GetMapping
	public ResponseEntity<GetMemberGradeResponse> getMemberGrade(@RequestHeader Long memberId) {
		return ResponseEntity.ok(memberGradeHistoryService.getMemberGrade(memberId));
	}
}
