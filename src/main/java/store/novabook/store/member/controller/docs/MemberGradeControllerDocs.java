package store.novabook.store.member.controller.docs;

import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import store.novabook.store.member.dto.response.GetMemberGradeResponse;

/**
 * 회원 등급 관련 API 요청을 처리하는 컨트롤러 문서화 인터페이스.
 */
@Tag(name = "MemberGrade API", description = "회원 등급 API")
public interface MemberGradeControllerDocs {

	/**
	 * 회원 등급 조회
	 *
	 * @param memberId 회원 ID
	 * @return 회원 등급 정보
	 */
	@Operation(summary = "회원 등급 조회", description = "회원의 등급 정보를 조회합니다.")
	@Parameter(description = "회원 ID")
	ResponseEntity<GetMemberGradeResponse> getMemberGrade(Long memberId);
}
