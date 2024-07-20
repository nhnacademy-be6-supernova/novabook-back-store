package store.novabook.store.member.controller.docs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import store.novabook.store.member.dto.request.DoorayAuthCodeRequest;
import store.novabook.store.member.dto.request.DoorayAuthRequest;

/**
 * 휴면회원 해지 시 사용하는 Dooray Hook 관련 API를 문서화하는 인터페이스입니다.
 */
@Tag(name = "DoorayHook API", description = "휴면회원 해지 시 사용하는 dooray hook 관련 API")
public interface DoorayHookControllerDocs {
	/**
	 * 인증 코드를 생성하여 Dooray를 통해 전송합니다.
	 *
	 * @param request Dooray 인증 요청 데이터
	 * @return 응답 상태 코드
	 */
	@Operation(summary = "인증 코드 전송", description = "인증 코드를 생성하여 Dooray를 통해 전송합니다.")
	@Parameter(description = "Dooray 인증 요청 데이터", required = true)
	ResponseEntity<Void> sendMessage(@Valid @RequestBody DoorayAuthRequest request);

	/**
	 * 인증 코드를 확인하고 휴면회원의 상태를 활성으로 업데이트합니다.
	 *
	 * @param request Dooray 인증 코드 요청 데이터
	 * @return 응답 상태 코드
	 */
	@Operation(summary = "휴면회원 상태 활성화", description = "인증 코드를 확인하고 휴면회원의 상태를 활성으로 업데이트합니다.")
	@Parameter(description = "Dooray 인증 코드 요청 데이터", required = true)
	ResponseEntity<Void> confirmDormantMember(@Valid @RequestBody DoorayAuthCodeRequest request);
}
