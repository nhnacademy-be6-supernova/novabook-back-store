package store.novabook.store.common.response;

import org.springframework.http.ProblemDetail;

import store.novabook.store.exception.ErrorCode;
import store.novabook.store.exception.NovaException;

/**
 * 에러 응답을 나타내는 레코드입니다.
 * 오류 코드와 메시지를 포함합니다.
 */
public record ErrorResponse(ErrorCode errorCode, String message) {

	/**
	 * {@link NovaException}에서 {@code ErrorResponse}를 생성합니다.
	 *
	 * @param novaException 변환할 예외
	 * @return 오류 코드와 메시지를 포함하는 {@code ErrorResponse}
	 */
	public static ErrorResponse from(NovaException novaException) {
		return ErrorResponse.from(novaException.getErrorCode());
	}

	/**
	 * {@link ErrorCode}에서 {@code ErrorResponse}를 생성합니다.
	 *
	 * @param errorCode 변환할 오류 코드
	 * @return 오류 코드와 메시지를 포함하는 {@code ErrorResponse}
	 */
	public static ErrorResponse from(ErrorCode errorCode) {
		return new ErrorResponse(errorCode, errorCode.getMessage());
	}

	public static ErrorResponse from(ProblemDetail problemDetail) {
		return new ErrorResponse(ErrorCode.PROBLEM_DETAIL, problemDetail.getDetail());
	}

	public static ErrorResponse from(ValidErrorResponse validErrorResponse) {
		return new ErrorResponse(ErrorCode.INVALID_REQUEST_ARGUMENT, validErrorResponse.message());
	}
}
