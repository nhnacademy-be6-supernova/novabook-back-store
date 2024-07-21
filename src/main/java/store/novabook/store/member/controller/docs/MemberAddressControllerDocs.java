package store.novabook.store.member.controller.docs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import store.novabook.store.member.dto.request.CreateMemberAddressRequest;
import store.novabook.store.member.dto.request.UpdateMemberAddressRequest;
import store.novabook.store.member.dto.response.CreateMemberAddressResponse;
import store.novabook.store.member.dto.response.ExceedResponse;
import store.novabook.store.member.dto.response.GetMemberAddressListResponse;
import store.novabook.store.member.dto.response.GetMemberAddressResponse;

/**
 * 회원 주소 관련 API 요청을 처리하는 컨트롤러 문서화 인터페이스입니다.
 * 회원의 주소를 생성, 조회, 수정, 삭제하는 API를 문서화합니다.
 */
@Tag(name = "MemberAddress API", description = "회원주소 API")
public interface MemberAddressControllerDocs {

	/**
	 * 새로운 회원 주소를 생성합니다.
	 *
	 * @param createMemberAddressRequest 회원 주소 생성 요청 정보
	 * @param memberId 회원 ID
	 * @return ResponseEntity<CreateMemberAddressResponse> 생성된 회원 주소 정보와 HTTP 상태 코드를 포함한 응답
	 */
	@Operation(summary = "회원 주소 생성", description = "새로운 회원 주소를 생성합니다.")
	@Parameter(description = "회원 주소 생성 요청 정보", required = true)
	@Parameter(description = "회원 ID", required = true)
	ResponseEntity<CreateMemberAddressResponse> createMemberAddress(
		@Valid @RequestBody CreateMemberAddressRequest createMemberAddressRequest, Long memberId);

	/**
	 * 특정 회원의 모든 주소를 조회합니다.
	 *
	 * @param memberId 회원 ID
	 * @return ResponseEntity<GetMemberAddressListResponse> 회원 주소 목록과 HTTP 상태 코드를 포함한 응답
	 */
	@Operation(summary = "회원 주소 목록 조회", description = "특정 회원의 모든 주소를 조회합니다.")
	@Parameter(description = "회원 ID", required = true)
	ResponseEntity<GetMemberAddressListResponse> getMemberAddressAll(Long memberId);

	/**
	 * 특정 회원 주소를 조회합니다.
	 *
	 * @param memberAddressId 회원 주소 ID
	 * @return ResponseEntity<GetMemberAddressResponse> 조회된 회원 주소 정보와 HTTP 상태 코드를 포함한 응답
	 */
	@Operation(summary = "회원 주소 조회", description = "특정 회원 주소를 조회합니다.")
	@Parameter(description = "회원 주소 ID", required = true)
	ResponseEntity<GetMemberAddressResponse> getMemberAddress(@PathVariable Long memberAddressId);

	/**
	 * 특정 회원 주소를 업데이트합니다.
	 *
	 * @param memberAddressId 회원 주소 ID
	 * @param updateMemberAddressRequest 주소 업데이트 요청 정보
	 * @return ResponseEntity<Void> 응답 상태 코드
	 */
	@Operation(summary = "회원 주소 업데이트", description = "특정 회원 주소를 업데이트합니다.")
	@Parameter(description = "회원 주소 ID", required = true)
	@Parameter(description = "주소 업데이트 요청 정보", required = true)
	ResponseEntity<Void> updateMemberAddress(@PathVariable Long memberAddressId,
		@RequestBody UpdateMemberAddressRequest updateMemberAddressRequest);

	/**
	 * 특정 회원 주소를 삭제합니다.
	 *
	 * @param memberAddressId 회원 주소 ID
	 * @return ResponseEntity<Void> 응답 상태 코드
	 */
	@Operation(summary = "회원 주소 삭제", description = "특정 회원 주소를 삭제합니다.")
	@Parameter(description = "회원 주소 ID", required = true)
	ResponseEntity<Void> deleteMemberAddress(@PathVariable Long memberAddressId);

	/**
	 * 회원 주소 개수의 초과 여부를 확인합니다.
	 *
	 * @param memberId 회원 ID
	 * @return ResponseEntity<ExceedResponse> 주소 개수 초과 여부와 HTTP 상태 코드를 포함한 응답
	 */
	@Operation(summary = "주소 개수 초과 여부 확인", description = "회원 주소 개수가 초과되었는지 확인합니다.")
	@Parameter(description = "회원 ID", required = true)
	ResponseEntity<ExceedResponse> isExceed(Long memberId);
}
