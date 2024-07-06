package store.novabook.store.common.response;

import static java.util.stream.Collectors.*;

import java.util.Map;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import store.novabook.store.exception.ErrorCode;

/**
 * 유효성 검사 오류에 대한 응답을 나타내는 레코드입니다.
 * 오류 코드, 메시지 및 필드별 오류 메시지 맵을 포함합니다.
 */
public record ValidErrorResponse(ErrorCode errorCode, String message, Map<String, String> result) {

	/**
	 * {@link MethodArgumentNotValidException}에서 {@code ValidErrorResponse}를 생성합니다.
	 *
	 * @param e 변환할 예외
	 * @return 오류 세부 정보를 포함하는 {@code ValidErrorResponse}
	 */
	public static ValidErrorResponse from(MethodArgumentNotValidException e) {
		Map<String, String> result = e.getBindingResult()
			.getFieldErrors()
			.stream()
			.collect(toMap(FieldError::getField, ValidErrorResponse::getFieldErrorMessage));
		return new ValidErrorResponse(ErrorCode.INVALID_REQUEST_ARGUMENT,
			ErrorCode.INVALID_REQUEST_ARGUMENT.getMessage(), result);
	}

	/**
	 * 주어진 {@link FieldError}에 대한 오류 메시지를 반환합니다.
	 *
	 * @param error 필드 오류
	 * @return 오류 메시지, 오류 메시지가 null인 경우 기본 메시지
	 */
	private static String getFieldErrorMessage(FieldError error) {
		String message = error.getDefaultMessage();
		if (message == null) {
			return "잘못된 요청입니다.";
		}
		return message;
	}
}
