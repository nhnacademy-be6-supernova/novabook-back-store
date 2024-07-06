package store.novabook.store.common.exception;

/**
 * 잘못된 요청 오류를 나타내기 위해 발생하는 예외로, HTTP 상태 코드 400에 해당합니다.
 * 클라이언트 오류로 인해 요청을 처리할 수 없을 때 이 예외를 던져야 합니다.
 * Nova Book Store 애플리케이션에 특정한 사용자 정의 오류 처리를 제공하기 위해 {@link NovaException}을 확장합니다.
 *
 * <p> 예제 사용법:
 * <pre>
 *     if (invalidRequest) {
 *         throw new BadRequestException(ErrorCode.INVALID_REQUEST_ARGUMENT);
 *     }
 * </pre>
 * </p>
 *
 * 이 예외는 글로벌 예외 처리기에서 받아서 클라이언트에게 적절한 HTTP 상태 코드와 메시지를 반환하는 데 사용됩니다.
 *
 * @see NovaException
 */
public class BadRequestException extends NovaException {
	/**
	 * 지정된 오류 코드를 사용하여 새로운 {@code BadRequestException}을 생성합니다.
	 *
	 * @param errorCode 발생한 오류에 대한 코드
	 */
	public BadRequestException(ErrorCode errorCode) {
		super(errorCode);
	}
}
