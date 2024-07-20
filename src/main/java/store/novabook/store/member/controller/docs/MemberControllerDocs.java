package store.novabook.store.member.controller.docs;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import store.novabook.store.member.dto.request.CreateMemberRequest;
import store.novabook.store.member.dto.request.DeleteMemberRequest;
import store.novabook.store.member.dto.request.DuplicateEmailRequest;
import store.novabook.store.member.dto.request.DuplicateLoginIdRequest;
import store.novabook.store.member.dto.request.FindMemberRequest;
import store.novabook.store.member.dto.request.GetDormantMembersRequest;
import store.novabook.store.member.dto.request.GetMembersUUIDRequest;
import store.novabook.store.member.dto.request.GetPaycoMembersRequest;
import store.novabook.store.member.dto.request.LinkPaycoMembersRequest;
import store.novabook.store.member.dto.request.LoginMemberRequest;
import store.novabook.store.member.dto.request.UpdateMemberPasswordRequest;
import store.novabook.store.member.dto.request.UpdateMemberRequest;
import store.novabook.store.member.dto.response.CreateMemberResponse;
import store.novabook.store.member.dto.response.DuplicateResponse;
import store.novabook.store.member.dto.response.FindMemberLoginResponse;
import store.novabook.store.member.dto.response.GetDormantMembersResponse;
import store.novabook.store.member.dto.response.GetMemberResponse;
import store.novabook.store.member.dto.response.GetMembersUUIDResponse;
import store.novabook.store.member.dto.response.GetPaycoMembersResponse;
import store.novabook.store.member.dto.response.GetmemberNameResponse;
import store.novabook.store.member.dto.response.LoginMemberResponse;

/**
 * 회원 관련 API 요청을 처리하는 컨트롤러 문서화 인터페이스.
 */
@Tag(name = "Member API", description = "회원의 API")
public interface MemberControllerDocs {

	/**
	 * 새로운 회원을 생성합니다.
	 *
	 * @param createMemberRequest 회원 생성 정보
	 * @return ResponseEntity<CreateMemberResponse> 생성된 회원 정보와 HTTP 상태 코드를 포함한 응답
	 */
	@Operation(summary = "회원 생성", description = "새로운 회원을 생성합니다.")
	@Parameter(description = "회원 생성 요청 정보", required = true)
	ResponseEntity<CreateMemberResponse> createMember(@Valid @RequestBody CreateMemberRequest createMemberRequest);

	/**
	 * 주어진 로그인 ID가 이미 사용 중인지 확인합니다.
	 *
	 * @param request 중복 체크할 로그인 ID
	 * @return ResponseEntity<DuplicateResponse> 로그인 ID 중복 여부
	 */
	@Operation(summary = "로그인 ID 중복 체크", description = "주어진 로그인 ID가 이미 사용 중인지 확인합니다.")
	@Parameter(description = "중복 체크할 로그인 ID", required = true)
	ResponseEntity<DuplicateResponse> checkDuplicateLoginId(@RequestBody DuplicateLoginIdRequest request);

	/**
	 * 주어진 이메일이 이미 사용 중인지 확인합니다.
	 *
	 * @param request 중복 체크할 이메일
	 * @return ResponseEntity<DuplicateResponse> 이메일 중복 여부
	 */
	@Operation(summary = "이메일 중복 체크", description = "주어진 이메일이 이미 사용 중인지 확인합니다.")
	@Parameter(description = "중복 체크할 이메일", required = true)
	ResponseEntity<DuplicateResponse> checkDuplicateEmail(@RequestBody DuplicateEmailRequest request);

	/**
	 * 모든 회원 정보를 페이징하여 조회합니다.
	 *
	 * @param pageable 페이징 정보
	 * @return ResponseEntity<Page < GetMemberResponse>> 조회된 회원 목록과 HTTP 상태 코드를 포함한 응답
	 */
	@Operation(summary = "모든 회원 조회", description = "모든 회원 정보를 페이징하여 조회합니다.")
	@Parameter(description = "페이징 정보", required = true)
	ResponseEntity<Page<GetMemberResponse>> getMemberAll(Pageable pageable);

	/**
	 * 주어진 회원 ID로 특정 회원 정보를 조회합니다.
	 *
	 * @param memberId 회원 ID
	 * @return ResponseEntity<GetMemberResponse> 조회된 회원 정보와 HTTP 상태 코드를 포함한 응답
	 */
	@Operation(summary = "회원 조회", description = "주어진 회원 ID로 특정 회원 정보를 조회합니다.")
	@Parameter(description = "회원 ID", required = false)
	ResponseEntity<GetMemberResponse> getMember(Long memberId);

	/**
	 * 주어진 회원 ID로 회원 이름을 조회합니다. 비회원일 경우 '비회원' 반환.
	 *
	 * @param memberId 회원 ID
	 * @return ResponseEntity<GetmemberNameResponse> 조회된 회원 이름과 HTTP 상태 코드를 포함한 응답
	 */
	@Operation(summary = "회원 이름 조회", description = "주어진 회원 ID로 회원 이름을 조회합니다. 비회원일 경우 '비회원' 반환.")
	@Parameter(description = "회원 ID", required = false)
	ResponseEntity<GetmemberNameResponse> getMemberName(Long memberId);

	/**
	 * 회원 로그인 요청을 처리합니다.
	 *
	 * @param loginMemberRequest 로그인 정보
	 * @return ResponseEntity<LoginMemberResponse> 로그인 응답과 HTTP 상태 코드를 포함한 응답
	 */
	@Operation(summary = "회원 로그인", description = "회원 로그인 요청을 처리합니다.")
	@Parameter(description = "로그인 요청 정보", required = true)
	ResponseEntity<LoginMemberResponse> login(@RequestBody LoginMemberRequest loginMemberRequest);

	/**
	 * 주어진 회원 ID로 회원 정보를 업데이트합니다.
	 *
	 * @param memberId 회원 ID
	 * @param updateMemberRequest 업데이트할 회원 정보
	 * @return ResponseEntity<Void> 응답 상태 코드
	 */
	@Operation(summary = "회원 정보 업데이트", description = "주어진 회원 ID로 회원 정보를 업데이트합니다.")
	@Parameter(description = "회원 ID", required = true)
	@Parameter(description = "업데이트할 회원 정보", required = true)
	ResponseEntity<Void> updateMember(Long memberId, @RequestBody UpdateMemberRequest updateMemberRequest);

	/**
	 * 주어진 회원 ID로 비밀번호를 업데이트합니다.
	 *
	 * @param memberId 회원 ID
	 * @param updateMemberPasswordRequest 업데이트할 비밀번호 정보
	 * @return ResponseEntity<Void> 응답 상태 코드
	 */
	@Operation(summary = "비밀번호 업데이트", description = "주어진 회원 ID로 비밀번호를 업데이트합니다.")
	@Parameter(description = "회원 ID", required = true)
	@Parameter(description = "업데이트할 비밀번호 정보", required = true)
	ResponseEntity<Void> updateMemberPassword(Long memberId,
		@RequestBody @Valid UpdateMemberPasswordRequest updateMemberPasswordRequest);

	/**
	 * 회원의 상태를 '휴면'으로 변경합니다.
	 *
	 * @param memberId 회원 ID
	 * @return ResponseEntity<Void> 응답 상태 코드
	 */
	@Operation(summary = "회원 상태를 휴면으로 변경", description = "회원의 상태를 '휴면'으로 변경합니다.")
	@Parameter(description = "회원 ID", required = true)
	ResponseEntity<Void> updateMemberStatusToDormant(Long memberId);

	/**
	 * 회원의 상태를 '탈퇴'로 변경합니다.
	 *
	 * @param memberId 회원 ID
	 * @param deleteMemberRequest 탈퇴 사유
	 * @return ResponseEntity<Void> 응답 상태 코드
	 */
	@Operation(summary = "회원 상태를 탈퇴로 변경", description = "회원의 상태를 '탈퇴'로 변경합니다.")
	@Parameter(description = "회원 ID", required = true)
	@Parameter(description = "회원 Password", required = true)
	ResponseEntity<Void> updateMemberStatusToWithdraw(Long memberId,
		@RequestBody DeleteMemberRequest deleteMemberRequest);

	/**
	 * 주어진 로그인 ID로 회원을 찾습니다.
	 *
	 * @param findMemberRequest 회원 찾기 정보
	 * @return ResponseEntity<FindMemberLoginResponse> 회원 찾기 응답과 HTTP 상태 코드를 포함한 응답
	 */
	@Operation(summary = "회원 찾기", description = "주어진 로그인 ID로 회원을 찾습니다.")
	@Parameter(description = "회원 찾기 요청 정보", required = true)
	ResponseEntity<FindMemberLoginResponse> find(@Valid @RequestBody FindMemberRequest findMemberRequest);

	/**
	 * 주어진 요청으로 Payco 회원 정보를 조회합니다.
	 *
	 * @param getPaycoMembersRequest Payco 회원 조회 정보
	 * @return ResponseEntity<GetPaycoMembersResponse> Payco 회원 정보와 HTTP 상태 코드를 포함한 응답
	 */
	@Operation(summary = "Payco 회원 조회", description = "주어진 요청으로 Payco 회원 정보를 조회합니다.")
	@Parameter(description = "Payco 회원 조회 요청 정보", required = true)
	ResponseEntity<GetPaycoMembersResponse> getPaycoMembers(
		@Valid @RequestBody GetPaycoMembersRequest getPaycoMembersRequest);

	/**
	 * Payco 회원을 연동합니다.
	 *
	 * @param linkPaycoMembersRequest Payco 회원 연동 정보
	 * @return ResponseEntity<Void> 응답 상태 코드
	 */
	@Operation(summary = "Payco 회원 연동", description = "Payco 회원을 연동합니다.")
	@Parameter(description = "Payco 회원 연동 요청 정보", required = true)
	ResponseEntity<Void> linkPayco(@Valid @RequestBody LinkPaycoMembersRequest linkPaycoMembersRequest);

	/**
	 * 회원 UUID를 조회합니다.
	 *
	 * @param getMembersUUIDRequest 회원 UUID 조회 정보
	 * @return ResponseEntity<GetMembersUUIDResponse> 회원 UUID와 HTTP 상태 코드를 포함한 응답
	 */
	@Operation(summary = "회원 UUID 조회", description = "회원 UUID를 조회합니다.")
	@Parameter(description = "회원 UUID 조회 요청 정보", required = true)
	ResponseEntity<GetMembersUUIDResponse> findUUID(@RequestHeader("Authorization") String authorization,
		@Valid @RequestBody GetMembersUUIDRequest getMembersUUIDRequest);

	/**
	 * 휴면 상태의 회원을 조회합니다.
	 *
	 * @param getDormantMembersRequest 휴면 회원 조회 정보
	 * @return ResponseEntity<GetDormantMembersResponse> 휴면 회원 목록과 HTTP 상태 코드를 포함한 응답
	 */
	@Operation(summary = "휴면 회원 조회", description = "휴면 상태의 회원을 조회합니다.")
	@Parameter(description = "휴면 회원 조회 요청 정보", required = true)
	ResponseEntity<GetDormantMembersResponse> getMemberDormantStatus(
		@Valid @RequestBody GetDormantMembersRequest getDormantMembersRequest);
}


